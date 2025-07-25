/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
