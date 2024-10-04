package org.hkurh.doky

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.hasEntry
import org.hkurh.doky.password.api.ResetPasswordRequest
import org.hkurh.doky.password.api.UpdatePasswordRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@DisplayName("Password API test")
class PasswordSpec : RestSpec() {

    private val endpoint = "$restPrefix/password"
    private val resetEndpoint = "$endpoint/reset"
    private val updateEndpoint = "$endpoint/update"
    private val nonExistUserEmail = "test-no-registered@test.com"
    private val validToken = "valid_token"


    @Test
    @DisplayName("Should process if no user with provided email")
    fun shouldProcess_whenNoUserWithProvidedEmail() {
        // given
        val requestBody = ResetPasswordRequest().apply {
            email = nonExistUserEmail
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(resetEndpoint)

        // then
        response.then().statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    @DisplayName("Should process if user with provided email exists")
    fun shouldProcess_whenUserExists() {
        // given
        val requestBody = ResetPasswordRequest().apply {
            email = validUserUid
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(resetEndpoint)

        // then
        response.then().statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    @DisplayName("Should update password when token is valid")
    @Sql(scripts = ["classpath:sql/PasswordSpec/setup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    fun shouldUpdatePassword_whenTokenIsValid() {
        // given
        val requestBody = UpdatePasswordRequest().apply {
            password = "6OiUl^BdDU"
            token = validToken
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(updateEndpoint)

        // then
        response.then().statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    @DisplayName("Should return BadRequest when password is empty")
    fun shouldReturnBadRequest_whenPasswordIsEmpty() {
        // given
        val requestBody = UpdatePasswordRequest().apply {
            password = ""
            token = validToken
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(updateEndpoint)

        // then
        response.then().statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", notNullValue())
            .body("error.message", notNullValue())
            .body("fields", hasItems(hasEntry("field", "password")))
    }
}
