package org.hkurh.doky

import io.restassured.RestAssured.given
import org.hkurh.doky.password.api.ResetPasswordRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@DisplayName("Password API test")
class PasswordSpec : RestSpec() {

    private val endpoint = "/password"
    private val resetEndpoint = "$endpoint/reset"
    private val nonExistUserEmail = "test-no-registered@test.com"


    @Test
    @DisplayName("Should return Not Fount if no user with provided email")
    fun shouldReturnNotFount_whenNoUserWithProvidedEmail() {
        // given
        val requestBody = ResetPasswordRequest().apply {
            email = nonExistUserEmail
        }
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(resetEndpoint)

        // then
        response.then().statusCode(HttpStatus.NOT_FOUND.value())
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

}
