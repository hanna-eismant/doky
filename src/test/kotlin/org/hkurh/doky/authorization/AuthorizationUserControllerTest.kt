/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

package org.hkurh.doky.authorization

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.security.JwtProvider
import org.hkurh.doky.users.UserFacade
import org.hkurh.doky.users.api.UserDto
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@DisplayName("AuthorizationUserController unit test")
class AuthorizationUserControllerTest : DokyUnitTest {

    private val userFacade: UserFacade = mock()
    private val jwtProvider: JwtProvider = mock()
    private var controller = AuthorizationUserController(userFacade, jwtProvider)

    @Test
    @DisplayName("Should generate token when login")
    fun shouldGenerateToken_whenLogin() {
        // given
        val userUid = "test@example.com"
        val userPassword = "password123"
        val authenticationRequest = AuthenticationRequest().apply {
            uid = userUid
            password = userPassword
        }
        whenever(userFacade.checkCredentials(userUid, userPassword)).thenReturn(
            UserDto().apply {
                uid = userUid
                roles = mutableSetOf("ROLE_USER")
            }
        )
        whenever(jwtProvider.generateToken(any(), any())).thenReturn("token")

        // when
        val response = controller.login(authenticationRequest)

        // then
        response.body!!.apply {
            assertFalse(token.isBlank())
        }
    }
}
