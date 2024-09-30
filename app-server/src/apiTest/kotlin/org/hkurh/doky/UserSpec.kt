package org.hkurh.doky

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.notNullValue
import org.hkurh.doky.users.api.UpdateUserRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@DisplayName("User API test")
class UserSpec : RestSpec() {

    private val endpoint = "$restPrefix/users/current"

    @Test
    @DisplayName("Should update username when it is provided")
    fun shouldUpdateUserName_whenUserNameProvided() {
        // given
        val requestBody = UpdateUserRequest().apply {
            name = "New Name"
        }
        val requestSpec = prepareRequestSpecWithLogin().setBody(requestBody).build()

        // when
        val response = given(requestSpec).put(endpoint)

        // then
        response.then().statusCode(HttpStatus.OK.value())
    }

    @Test
    @DisplayName("Should return error when update user data with empty username")
    fun shouldReturnError_whenUpdateUserDataWithEmptyUsername() {
        // given
        val requestBody = UpdateUserRequest()
        val requestSpec = prepareRequestSpecWithLogin().setBody(requestBody).build()

        // when
        val response = given(requestSpec).put(endpoint)

        // then
        response.then().statusCode(HttpStatus.BAD_REQUEST.value())
            .body("error", notNullValue())
            .body("error.message", notNullValue())
    }
}
