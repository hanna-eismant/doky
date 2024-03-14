package org.hkurh.doky.security

import jakarta.servlet.http.HttpServletResponse
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.security.JwtProvider.generateToken
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder


@ExtendWith(MockitoExtension::class)
@DisplayName("JwtAuthorizationFilter unit test")
class JwtAuthorizationFilterTest {
    private val response = MockHttpServletResponse()
    private val authorizationHeader = "Authorization"
    private val userUid = "user@mail.com"
    private val expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkb2t5VG9rZW4iLCJzdWIiOiJoYW5uYV90ZXN0XzNAZXhhbXBsZS5jb20iLCJpYXQiOjE3MDk3OTg1NjIsImV4cCI6MTcwOTg4NDk2Mn0.CJK6Utq0pAd-yWMKjJhLj8On1_6Dt9jHsqj0zQa6o0A"

    @InjectMocks
    @Spy
    private val filter: JwtAuthorizationFilter? = null
    private val userService: UserService = mock()
    private val filterChain: MockFilterChain = mock()

    @Test
    @DisplayName("Should add user to security context when token is valid")
    fun shouldAddUserToSecurityContext_whenTokenIsValid() {
        // given
        val token = generateToken(userUid)
        val request = MockHttpServletRequest()
        request.addHeader(authorizationHeader, "Bearer $token")

        val userEntity = UserEntity().apply {
            uid = userUid
        }
        whenever(userService.findUserByUid(userUid)).thenReturn(userEntity)

        // when
        filter?.doFilter(request, response, filterChain)

        // then
        verify(filterChain).doFilter(request, response)
        verify(userService).findUserByUid(userUid)
        val contextUser =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()
        assertTrue(contextUser == userEntity)
    }

    @Test
    @DisplayName("Should clear security context when token is not presented")
    fun shouldClearSecurityContext_whenNoToken() {
        // given
        val request = MockHttpServletRequest()

        // when
        filter?.doFilter(request, response, filterChain)

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
        val token = generateToken(userUid)
        val request = MockHttpServletRequest()
        request.addHeader(authorizationHeader, "Bearer $token")
        whenever(userService.findUserByUid(userUid)).thenThrow(DokyNotFoundException::class.java)

        // when
        filter?.doFilter(request, response, filterChain)

        // then
        assertTrue(response.status == HttpServletResponse.SC_FORBIDDEN)
    }

    @Test
    @DisplayName("Should set response error status when token is incorrect")
    fun shouldSetResponseError_whenIncorrectToken() {
        // given
        val request = MockHttpServletRequest()
        request.addHeader(authorizationHeader, "Bearer token")

        // when
        filter?.doFilter(request, response, filterChain)

        // then
        verify(userService, never()).findUserByUid(any())
        assertTrue(response.status == HttpServletResponse.SC_FORBIDDEN)
    }

    @Test
    @DisplayName("Should set response error status when token is expired")
    fun shouldSetResponseError_whenExpiredToken() {
        // given
        val request = MockHttpServletRequest()
        request.addHeader(authorizationHeader, "Bearer $expiredToken")

        // when
        filter?.doFilter(request, response, filterChain)

        // then
        verify(userService, never()).findUserByUid(any())
        assertTrue(response.status == HttpServletResponse.SC_FORBIDDEN)
    }
}
