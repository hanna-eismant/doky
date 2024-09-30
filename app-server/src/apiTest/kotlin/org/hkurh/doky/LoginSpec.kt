package org.hkurh.doky

import io.restassured.RestAssured.given
import org.apache.commons.lang3.StringUtils
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.hasEntry
import org.hkurh.doky.authorization.AuthenticationRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@DisplayName("Login API test")
class LoginSpec : RestSpec() {
    val endpoint = "$restPrefix/login"
    val incorrectUserUid = "test_1234@example.com"
    val incorrectUserPassword = "pass-12345"

    @Test
    @DisplayName("Should receive token for valid user when login")
    fun shouldReceiveTokenWhenLoginValidUser() {
        // given
        val requestBody = AuthenticationRequest().apply {
            uid = validUserUid
            password = validUserPassword
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.OK.value())
            .body("token", notNullValue())
    }

    @Test
    @DisplayName("Should return error when login with non-exist user")
    fun shouldReturnErrorWhenLoginNonExistingUser() {
        // given
        val requestBody = AuthenticationRequest().apply {
            uid = incorrectUserUid
            password = incorrectUserPassword
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.UNAUTHORIZED.value())
            .body("error", notNullValue())
            .body("error.message", notNullValue())
    }

    @Test
    @DisplayName("Should return error when credentials are empty")
    fun shouldReturnErrorWhenLoginEmptyCredentials() {
        // given
        val requestBody = AuthenticationRequest().apply {
            uid = StringUtils.EMPTY
            password = StringUtils.EMPTY
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", notNullValue())
            .body("error.message", notNullValue())
            .body("fields", hasItems(hasEntry("field", "uid"), hasEntry("field", "password")))
    }
}
