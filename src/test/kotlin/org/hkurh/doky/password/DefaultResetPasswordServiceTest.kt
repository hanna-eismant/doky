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

package org.hkurh.doky.password

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.password.impl.DefaultResetPasswordService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*


@DisplayName("DefaultResetPasswordService unit test")
class DefaultResetPasswordServiceTest : DokyUnitTest {

    private val fixedInstant = Instant.parse("2022-01-01T00:00:00.00Z")
    private val clock = Clock.fixed(fixedInstant, ZoneId.of("UTC"))
    private val passwordTokenService: PasswordTokenService = mock()
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository = mock()
    private val resetPasswordService = DefaultResetPasswordService(
        passwordTokenService,
        resetPasswordTokenEntityRepository, clock
    )

    private val testUser = UserEntity()
    private val tokenString = "test-token"

    @Test
    @DisplayName("Should save token info in database")
    fun shouldSaveTokenInfoInDatabase() {
        // given
        val tokenId: Long = 1
        val tokenDate = Date()
        whenever(passwordTokenService.generateToken()).thenReturn(tokenString)
        whenever(passwordTokenService.calculateExpirationDate()).thenReturn(tokenDate)

        whenever(resetPasswordTokenEntityRepository.save(any<ResetPasswordTokenEntity>())).thenAnswer { invocation ->
            val argument = invocation.getArgument<ResetPasswordTokenEntity>(0)
            assertAll(
                { assertEquals(testUser, argument.user) },
                { assertEquals(tokenString, argument.token) },
                { assertEquals(tokenDate, argument.expirationDate) }
            )
            argument.id = tokenId
            argument
        }

        // when
        val token = resetPasswordService.generateAndSaveResetToken(testUser)

        // then
        assertEquals(token, tokenString)
        verify(resetPasswordTokenEntityRepository, times(1)).save(any<ResetPasswordTokenEntity>())
    }

    @Test
    @DisplayName("Should return INVALID when reset password token is not found")
    fun shouldReturnInvalid_whenResetPasswordTokenNotFound() {
        // given
        val token = "token"
        whenever(resetPasswordTokenEntityRepository.findByToken(token)).thenReturn(null)

        // when
        val actualStatus = resetPasswordService.validateToken(tokenString)

        // then
        assertEquals(TokenStatus.INVALID, actualStatus)
    }

    @Test
    @DisplayName("Should return EXPIRED when reset password token is expired")
    fun shouldReturnExpired_whenResetPasswordTokenExpired() {
        // given
        val token = "token"
        val tokenUser = UserEntity().apply {
            this.uid = "user@mail.test"
        }
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.expirationDate = Date.from(Instant.parse("2021-01-01T00:03:00Z"))
            this.user = tokenUser
        }
        whenever(resetPasswordTokenEntityRepository.findByToken(token)).thenReturn(resetPasswordTokenEntity)

        // when
        val actualStatus = resetPasswordService.validateToken(token)

        // then
        assertEquals(TokenStatus.EXPIRED, actualStatus)
    }

    @Test
    @DisplayName("Should return VALID password token entity when token is valid")
    fun shouldReturnValid_whenTokenIsValid() {
        // given
        val token = "token"
        val tokenUser = UserEntity().apply {
            this.uid = "user@mail.test"
        }
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.expirationDate = Date(System.currentTimeMillis() + 300_000)
            this.user = tokenUser
        }
        whenever(resetPasswordTokenEntityRepository.findByToken(token)).thenReturn(resetPasswordTokenEntity)

        // when
        val actualStatus = resetPasswordService.validateToken(token)

        // then
        assertEquals(TokenStatus.VALID, actualStatus)
    }

    @Test
    @DisplayName("Should get user from token when exists")
    fun shouldGetUserFromToken_whenExists() {
        // given
        val tokenEntity = ResetPasswordTokenEntity().apply {
            user = testUser
        }
        whenever(resetPasswordTokenEntityRepository.findByToken(tokenString)).thenReturn(tokenEntity)

        // when
        val actualUser = resetPasswordService.getUserForToken(tokenString)

        // then
        assertEquals(testUser, actualUser)
    }

    @Test
    @DisplayName("Should throw exception when get user from token and does not exists")
    fun shouldThrowException_whenGetUserFromToken_whenDoesNotExist() {
        // given
        whenever(resetPasswordTokenEntityRepository.findByToken(tokenString)).thenReturn(null)

        // when - then
        assertThrows<DokyNotFoundException> { resetPasswordService.getUserForToken(tokenString) }
    }
}
