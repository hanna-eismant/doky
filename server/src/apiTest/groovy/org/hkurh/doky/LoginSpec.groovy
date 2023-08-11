package org.hkurh.doky

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.security.AuthenticationRequest
import org.junit.Test
import org.springframework.http.HttpStatus

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.*

class LoginSpec extends RestSpec {
    private static ENDPOINT = '/login'
    private static INCORRECT_USER_UID = 'test_1234'
    private static INCORRECT_USER_PASSWORD = 'pass-12345'

    @Test
    void 'Should receive token for valid user when login'() {
        given:
        def requestBody = new AuthenticationRequest(VALID_USER_UID, VALID_USER_PASSWORD)
        and:
        def requestSpec = prepareRequestSpec().setBody(requestBody).build()

        when:
        def response = given(requestSpec).post(ENDPOINT)

        then:
        response.then().statusCode(HttpStatus.OK.value())
                .body('token', notNullValue())
    }

    @Test
    void 'Should return error when login with non-exist user'() {
        given:
        def requestBody = new AuthenticationRequest(INCORRECT_USER_UID, INCORRECT_USER_PASSWORD)
        and:
        def requestSpec = prepareRequestSpec().setBody(requestBody).build()

        when:
        def response = given(requestSpec).post(ENDPOINT)

        then:
        response.then().statusCode(HttpStatus.UNAUTHORIZED.value())
                .body('error', notNullValue())
                .body('error.message', notNullValue())
    }

    @Test
    void 'Should return error when credentials are empty'() {
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
                .body('fields', hasItems(hasEntry('field', 'username'), hasEntry('field', 'password')))
    }
}
