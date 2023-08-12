package org.hkurh.doky

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.authorization.AuthenticationRequest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.notNullValue

class RegistrationSpec extends RestSpec {
    private static final NEW_USER_UID = 'new_user_test'
    private static final NEW_USER_PASSWORD = 'Qwert!2345'
    private static final ENDPOINT = '/register'

    @Test
    @Sql(scripts = "classpath:sql/RegistrationSpec/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void 'Should register user when it does not exist'() {
        given:
        def requestBody = new AuthenticationRequest(NEW_USER_UID, NEW_USER_PASSWORD)
        and:
        def requestSpec = prepareRequestSpec().setBody(requestBody).build()

        when:
        def response = given(requestSpec).post(ENDPOINT)

        then:
        response.then().statusCode(HttpStatus.CREATED.value())
                .header(LOCATION_HEADER, notNullValue())
    }

    @Test
    void 'Should return error when register with existing user'() {
        given:
        def requestBody = new AuthenticationRequest(VALID_USER_UID, NEW_USER_PASSWORD)
        and:
        def requestSpec = prepareRequestSpec().setBody(requestBody).build()

        when:
        def response = given(requestSpec).post(ENDPOINT)

        then:
        response.then().statusCode(HttpStatus.CONFLICT.value())
                .body('error', notNullValue())
                .body('error.message', notNullValue())
    }

    @Test
    void 'Should return error when register with empty credentials'() {
        given:
        def requestBody = new AuthenticationRequest(StringUtils.EMPTY, StringUtils.EMPTY)
        and:
        def requestSpec = prepareRequestSpec().setBody(requestBody).build()

        when:
        def response = given(requestSpec).post(ENDPOINT)

        then:
        response.then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body('error', notNullValue())
                .body('error.message', notNullValue())
    }
}
