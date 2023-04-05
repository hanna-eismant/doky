package org.hkurh.doky

import org.hkurh.doky.controllers.data.DocumentRequest
import org.hkurh.doky.dto.DocumentDto
import org.hkurh.doky.repositories.DocumentEntityRepository
import org.hkurh.doky.repositories.UserEntityRepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

import java.sql.Types

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.comparesEqualTo
import static org.hamcrest.Matchers.notNullValue

@Sql(scripts = "classpath:sql/DocumentSpec/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DocumentSpec extends RestSpec {
    private static ENDPOINT = '/documents'
    private static ENDPOINT_SINGLE = '/documents/{id}'
    private static DOCUMENT_ID_PROPERTY = 'id'
    private static DOCUMENT_NAME_PROPERTY = 'name'
    private static EXISTED_DOCUMENT_NAME_FIRST = 'Test_1'
    private static EXISTED_DOCUMENT_NAME_SECOND = 'Test_2'
    private static EXISTED_DOCUMENT_NAME_THRID = 'Test_3'
    private static EXISTED_DOCUMENT_NAME_FOUR = 'Test_4'
    private static NEW_DOCUMENT_NAME = 'Apples'
    private static NEW_DOCUMENT_DESCRIPTION = 'Do you like apples?'

    @Autowired
    private DocumentEntityRepository documentEntityRepository
    @Autowired
    private UserEntityRepository userEntityRepository

    @Test
    def 'Should create new document'() {
        given:
        def requestBody = new DocumentRequest(NEW_DOCUMENT_NAME, NEW_DOCUMENT_DESCRIPTION)
        and:
        def requestSpec = prepareRequestSpecWithLogin()
                .setBody(requestBody)
                .build()

        when:
        def response = given(requestSpec).post(ENDPOINT)

        then:
        response.then().statusCode(HttpStatus.CREATED.value())
                .header(LOCATION_HEADER, notNullValue())
    }

    @Test
    @Sql(scripts = "classpath:sql/DocumentSpec/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    def 'Should return document by id when exists'() {
        given:
        def docId = getDocumentId(EXISTED_DOCUMENT_NAME_FIRST) as Long
        and:
        def requestSpec = prepareRequestSpecWithLogin()
                .addPathParam(DOCUMENT_ID_PROPERTY, docId)
                .build()

        when:
        def response = given(requestSpec).get(ENDPOINT_SINGLE)

        then:
        response.then().statusCode(HttpStatus.OK.value())
        and:
        def name = response.path(DOCUMENT_NAME_PROPERTY) as String
        EXISTED_DOCUMENT_NAME_FIRST == name
        and:
        def id = response.path(DOCUMENT_ID_PROPERTY) as Long
        docId comparesEqualTo(id)
    }

    @Test
    @Sql(scripts = 'classpath:sql/DocumentSpec/setup.sql', executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    def 'Should get all existing documents for user'() {
        given:
        def requestSpec = prepareRequestSpecWithLogin()
                .build()

        when:
        def response = given(requestSpec).get(ENDPOINT)

        then:
        response.then().statusCode(HttpStatus.OK.value())
        and:
        def documents = response.path(".") as DocumentDto[]
        documents.find { d -> (EXISTED_DOCUMENT_NAME_FIRST == d.name) } != null
        documents.find { d -> (EXISTED_DOCUMENT_NAME_SECOND == d.name) } != null
        and:
        documents.find { d -> (EXISTED_DOCUMENT_NAME_THRID == d.name) } == null
        documents.find { d -> (EXISTED_DOCUMENT_NAME_FOUR == d.name) } == null
    }

    @Test
    @Sql(scripts = 'classpath:sql/DocumentSpec/setup.sql', executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    def 'Should return error when get existing document that belongs to another user'() {
        given:
        def docId = getDocumentId(EXISTED_DOCUMENT_NAME_THRID) as Long
        and:
        def requestSpec = prepareRequestSpecWithLogin()
                .addPathParam(DOCUMENT_ID_PROPERTY, docId)
                .build()

        when:
        def response = given(requestSpec).get(ENDPOINT_SINGLE)

        then:
        response.then().statusCode(HttpStatus.NO_CONTENT.value())
    }

    def getDocumentId(docName) {
        def existedDocumentQuery = 'select d.id from document d where d.name = ?'
        def args = new Object[]{docName}
        def argTypes = new int[]{Types.VARCHAR}
        jdbcTemplate.queryForObject(existedDocumentQuery, args, argTypes, Long.class)
    }
}
