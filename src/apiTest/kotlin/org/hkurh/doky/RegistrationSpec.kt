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
import org.apache.commons.lang3.StringUtils
import org.hamcrest.CoreMatchers.notNullValue
import org.hkurh.doky.authorization.AuthenticationRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql


@DisplayName("Registration API test")
class RegistrationSpec : RestSpec() {
    val endpoint = "$restPrefix/register"
    val newUserUid = "new_user_test@example.com"
    val newUserPassword = "Qwert!2345"

    @Test
    @DisplayName("Should register user when it does not exist")
    @Sql(
        scripts = ["classpath:sql/RegistrationSpec/cleanup.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun shouldRegisterUserWhenItDoesNotExist() {
        // given
        val requestBody = AuthenticationRequest().apply {
            uid = newUserUid
            password = newUserPassword
        }
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
        val requestBody = AuthenticationRequest().apply {
            uid = validUserUid
            password = newUserPassword
        }
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
    }
}
