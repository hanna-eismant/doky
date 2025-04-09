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
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.security

import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletResponse
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.security.impl.JwtAuthorizationFilter
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.AuthorityEntity
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder


@DisplayName("JwtAuthorizationFilter unit test")
class JwtAuthorizationFilterTest : DokyUnitTest {
    private val response = MockHttpServletResponse()
    private val authorizationHeader = "Authorization"
    private val userUid = "user@mail.com"
    private val validToken =
        "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl9pZCI6ImRva3lBdXRoVG9rZW4iLCJ1c2VybmFtZSI6ImhrdXJoNEBvdXRsb29rLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NDQyMDY1MzEsImV4cCI6MjA1OTczOTMzMX0.VLXtfO_dHTQfHoSdJ7Tg4HnU4zFlJnXonvuHoTFzluU"

    private val userService: UserService = mock()
    private val filterChain: MockFilterChain = mock()
    private val jwtProvider: JwtProvider = mock()
    private var filter = JwtAuthorizationFilter(userService, jwtProvider)

    @Test
    @DisplayName("Should add user to security context when token is valid")
    fun shouldAddUserToSecurityContext_whenTokenIsValid() {
        // given
        val userEntity = UserEntity().apply {
            uid = userUid
            authorities = mutableSetOf(AuthorityEntity())
        }
        val request = MockHttpServletRequest()
        request.addHeader(authorizationHeader, "Bearer $validToken")
        whenever(jwtProvider.getUsernameFromToken(any())).thenReturn(userUid)
        whenever(userService.findUserByUid(userUid)).thenReturn(userEntity)

        // when
        filter.doFilter(request, response, filterChain)

        // then
        verify(filterChain).doFilter(request, response)
        verify(userService).findUserByUid(userUid)
        val contextUser =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).userEntity
        assertTrue(contextUser == userEntity)
    }

    @Test
    @DisplayName("Should clear security context when token is not presented")
    fun shouldClearSecurityContext_whenNoToken() {
        // given
        val request = MockHttpServletRequest()

        // when
        filter.doFilter(request, response, filterChain)

        // then
        verify(filterChain).doFilter(request, response)
        verify(userService, never()).findUserByUid(any())
        val auth = SecurityContextHolder.getContext().authentication
        assertTrue(auth == null)
    }

    @Test
    @DisplayName("Should set response error status when user is incorrect")
    fun shouldSetResponseError_whenIncorrectUser() {
        // given
        val request = MockHttpServletRequest()
        request.addHeader(authorizationHeader, "Bearer $validToken")
        whenever(jwtProvider.getUsernameFromToken(any())).thenReturn(userUid)
        whenever(userService.findUserByUid(userUid)).thenThrow(DokyNotFoundException::class.java)

        // when
        filter.doFilter(request, response, filterChain)

        // then
        assertTrue(response.status == HttpServletResponse.SC_FORBIDDEN)
    }

    @Test
    @DisplayName("Should set response error status when token is incorrect")
    fun shouldSetResponseError_whenIncorrectToken() {
        // given
        val request = MockHttpServletRequest()
        request.addHeader(authorizationHeader, "Bearer token")
        whenever(jwtProvider.getUsernameFromToken(any())).thenThrow(JwtException("Invalid"))

        // when
        filter.doFilter(request, response, filterChain)

        // then
        verify(userService, never()).findUserByUid(any())
        assertTrue(response.status == HttpServletResponse.SC_FORBIDDEN)
    }
}
