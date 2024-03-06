package org.hkurh.doky

import io.restassured.RestAssured.given
import org.apache.commons.lang3.StringUtils
import org.hamcrest.CoreMatchers.notNullValue
import org.hkurh.doky.authorization.AuthenticationRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class RegistrationSpec : RestSpec() {
    val newUserUid = "new_user_test@example.com"
    val newUserPassword = "Qwert!2345"
    val endpoint = "/register"

    @Test
    @DisplayName("Should register user when it does not exist")
    @Sql(
        scripts = ["classpath:sql/RegistrationSpec/cleanup.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun shouldRegisterUserWhenItDoesNotExist() {
        // given
        val requestBody = AuthenticationRequest(newUserUid, newUserPassword)
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.CREATED.value())
            .header(locationHeader, notNullValue())
    }

    @Test
    @DisplayName("Should return error when register with existing user")
    fun shouldReturnErrorWhenRegisterExistingUser() {
        // given
        val requestBody = AuthenticationRequest(validUserUid, newUserPassword)
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.CONFLICT.value())
            .body("error", notNullValue())
            .body("error.message", notNullValue())
    }

    @Test
    @DisplayName("Should return error when register with empty credentials")
    fun shouldReturnErrorWhenRegisterWithEmptyCredentials() {
        // given
        val requestBody = AuthenticationRequest(StringUtils.EMPTY, StringUtils.EMPTY)
        val requestSpec = prepareRequestSpec().setBody(requestBody).build()

        // when
        val response = given(requestSpec).post(endpoint)

        // then
        response.then().statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", notNullValue())
            .body("error.message", notNullValue())
    }
}
