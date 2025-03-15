/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.notNullValue
import org.hkurh.doky.documents.api.DocumentRequest
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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
    val endpoint = "$restPrefix/documents"
    val endpointSingle = "$endpoint/{id}"
    val endpointDownloadToken = "$endpoint/{id}/download/token"
    val endpointUpload = "$endpointSingle/upload"
    val documentIdProperty = "id"
    val documentNameProperty = "name"
    val documentFileNameProperty = "fileName"
    val tokenProperty = "token"
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
        val documentId = getDocumentId(existedDocumentNameFirst)
        val requestSpec = prepareRequestSpecWithLogin()
            .addPathParam(documentIdProperty, documentId)
            .build()

        // when
        val response = given(requestSpec).get(endpointSingle)

        // then

        assertAll(
            "Response",
            { assertEquals(existedDocumentNameFirst, response.path(documentNameProperty)) },
            { assertEquals(existedDocumentNameFirst, response.path(documentNameProperty)) },
            { assertEquals(documentId, response.path(documentIdProperty)) },
            { assertEquals(existedDocumentFileNameFirst, response.path(documentFileNameProperty)) }
        )
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
        val docId = getDocumentId(existedDocumentNameThird)
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

    @Test
    @DisplayName("Should generate download token")
    @Sql(scripts = ["classpath:sql/DocumentSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun shouldGenerateDownloadToken() {
        // given
        val documentId = getDocumentId(existedDocumentNameFirst)
        val requestSpec = prepareRequestSpecWithLogin()
            .addPathParam(documentIdProperty, documentId)
            .build()

        // when
        val response = given(requestSpec).get(endpointDownloadToken)

        // then
        val token: String = response.path(tokenProperty)
        val tokenRow = getDownloadToken(token)

        assertAll(
            "Response",
            { response.then().statusCode(HttpStatus.OK.value()) },
            { assertEquals(tokenRow.app_user, getUserId(validUserUid)) },
            { assertEquals(tokenRow.document, documentId) }
        )
    }

    @Test
    @DisplayName("Should return 404 when generate download token for non existing document")
    @Sql(scripts = ["classpath:sql/DocumentSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun shouldReturn404WhenGenerateDownloadTokenForNonExistingDocument() {
        // given
        val documentId = getDocumentId(existedDocumentNameThird)
        val requestSpec = prepareRequestSpecWithLogin()
            .addPathParam(documentIdProperty, documentId)
            .build()

        // when
        val response = given(requestSpec).get(endpointDownloadToken)

        // then
        response.then().statusCode(HttpStatus.NOT_FOUND.value())
    }

    fun getDocumentId(documentName: String): Long {
        val existedDocumentQuery = "select d.id from documents d where d.name = ?"
        val args = arrayOf(documentName)
        val argTypes = intArrayOf(Types.VARCHAR)
        val documentId = jdbcTemplate.queryForObject(existedDocumentQuery, args, argTypes, Long::class.java)
        return documentId ?: throw IllegalArgumentException("Document not found with name: $documentName")
    }

    fun getUserId(userUid: String): Long {
        val existedDocumentQuery = "select u.id from users u where u.uid = ?"
        val args = arrayOf(userUid)
        val argTypes = intArrayOf(Types.VARCHAR)
        val userId = jdbcTemplate.queryForObject(existedDocumentQuery, args, argTypes, Long::class.java)
        return userId ?: throw IllegalArgumentException("User not found with UID: $userUid")
    }

    fun getDownloadToken(token: String): DownloadDocumentTokenRow {
        val downloadTokenQuery = "select * from download_document_tokens dt where dt.token = ?"
        val args = arrayOf(token)
        val argTypes = intArrayOf(Types.VARCHAR)
        return jdbcTemplate.queryForObject(downloadTokenQuery, args, argTypes) { rs, _ ->
            DownloadDocumentTokenRow(
                token = rs.getString("token"),
                document = rs.getLong("document"),
                app_user = rs.getLong("app_user")
            )
        } ?: throw IllegalArgumentException("Download token not found for token: $token")
    }

    fun createFileToUpload(): File? {
        temporaryFolder.create()
        val file = temporaryFolder.newFile(uploadFileName)
        file.appendText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt")
        file.appendText(" ut labore et dolore magna aliqua.")
        return file
    }
}

data class DownloadDocumentTokenRow(
    val token: String,
    val document: Long,
    val app_user: Long
)
