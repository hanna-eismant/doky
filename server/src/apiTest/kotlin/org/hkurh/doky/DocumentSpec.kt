package org.hkurh.doky

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.notNullValue
import org.hkurh.doky.documents.api.DocumentRequest
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.rules.TemporaryFolder
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Paths
import java.sql.Types

@Sql(scripts = ["classpath:sql/DocumentSpec/cleanup.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("Documents API test")
class DocumentSpec : RestSpec() {
    val endpoint = "/documents"
    val endpointSingle = "$endpoint/{id}"
    val endpointUpload = "$endpointSingle/upload"
    val documentIdProperty = "id"
    val documentNameProperty = "name"
    val documentFileNameProperty = "fileName"
    val existedDocumentNameFirst = "Test_1"
    val existedDocumentFileNameFirst = "test.txt"
    val existedDocumentNameThird = "Test_3"
    val newDocumentName = "Apples"
    val newDocumentDescription = "Do you like apples?"
    val uploadFileName = "test_1.txt"

    @Value("\${doky.filestorage.path}")
    lateinit var fileStoragePath: String

    @Rule
    var temporaryFolder = TemporaryFolder()

    @AfterEach
    fun tearDown() {
        fileStoragePath.let {
            FileSystemUtils.deleteRecursively(Paths.get(it))
        }
    }

    @Test
    @DisplayName("Should create new document")
    fun shouldCreateNewDocument() {
        // given
        val requestBody = DocumentRequest().apply {
            name = newDocumentName
            description = newDocumentDescription
        }
        val requestSpec = prepareRequestSpecWithLogin().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.CREATED.value())
            .header(locationHeader, notNullValue())
    }

    @Test
    @DisplayName("Should return document by id when exists")
    @Sql(scripts = ["classpath:sql/DocumentSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun shouldReturnDocumentWhenExists() {
        // given
        val docId = getDocumentId(existedDocumentNameFirst)
        val requestSpec = prepareRequestSpecWithLogin()
            .addPathParam(documentIdProperty, docId)
            .build()

        // when
        val response = given(requestSpec).get(endpointSingle)

        // then
        response.then().statusCode(HttpStatus.OK.value())
        val name: String = response.path(documentNameProperty)
        assertEquals(existedDocumentNameFirst, name)
        val id: Long = response.path(documentIdProperty)
        assertEquals(docId, id)
        val fileName: String = response.path(documentFileNameProperty)
        assertEquals(existedDocumentFileNameFirst, fileName)
    }

    @Test
    @DisplayName("Should return 404 when get existing document that belongs to another user")
    @Sql(scripts = ["classpath:sql/DocumentSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun shouldReturn404whenGetDocumentAnotherUser() {
        // given
        val docId = getDocumentId(existedDocumentNameThird)
        val requestSpec = prepareRequestSpecWithLogin()
            .addPathParam(documentIdProperty, docId)
            .build()

        // when
        val response = given(requestSpec).get(endpointSingle)

        // then
        response.then().statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    @DisplayName("Should upload file for existing document")
    @Sql(scripts = ["classpath:sql/DocumentSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun shouldUploadFileWhenDocumentExists() {
        // given
        val docId = getDocumentId(existedDocumentNameFirst)
        val requestSpec = prepareRequestSpecWithLogin()
            .addPathParam(documentIdProperty, docId)
            .addMultiPart("file", createFileToUpload(), "text/plain")
            .addHeader(contentTypeHeader, "multipart/form-data")
            .build()

        // when
        val response = given(requestSpec).post(endpointUpload)

        // then
        response.then().statusCode(HttpStatus.OK.value())
    }

    @Test
    @DisplayName("Should return 404 when upload file for document that does not belong to current customer")
    @Sql(scripts = ["classpath:sql/DocumentSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun shouldReturnErrorWhenUploadFileToNonExistingDocument() {
        // given
        val docId = getDocumentId(existedDocumentNameThird) as Long
        val requestSpec = prepareRequestSpecWithLogin()
            .addPathParam(documentIdProperty, docId)
            .addMultiPart("file", createFileToUpload(), "text/plain")
            .addHeader(contentTypeHeader, "multipart/form-data")
            .build()

        // when
        val response = given(requestSpec).post(endpointUpload)

        // then
        response.then().statusCode(HttpStatus.NOT_FOUND.value())
    }


    @Test
    @DisplayName("Should return error 400 when sending empty attributes in document request")
    fun shouldReturnError400WhenSendingEmptyAttributesInDocumentRequest() {
        // given
        val requestBodyEmpty = DocumentRequest().apply {
            name = ""
            description = ""
        }
        val requestSpec = prepareRequestSpecWithLogin().setBody(requestBodyEmpty).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.BAD_REQUEST.value())
    }

    fun getDocumentId(docName: String): Long? {
        val existedDocumentQuery = "select d.id from documents d where d.name = ?"
        val args = arrayOf(docName)
        val argTypes = intArrayOf(Types.VARCHAR)
        return jdbcTemplate.queryForObject(existedDocumentQuery, args, argTypes, Long::class.java)
    }

    fun createFileToUpload(): File? {
        temporaryFolder.create()
        val file = temporaryFolder.newFile(uploadFileName)
        file.appendText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt")
        file.appendText(" ut labore et dolore magna aliqua.")
        return file
    }
}
