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

package org.hkurh.doky.password

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.errorhandling.DokyInvalidTokenException
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.password.impl.DefaultResetPasswordService
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*


@DisplayName("DefaultResetPasswordService unit test")
class DefaultResetPasswordServiceTest : DokyUnitTest {

    private var tokenService: TokenService = mock()
    private var userService: UserService = mock()
    private var resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository = mock()
    private var resetPasswordService = DefaultResetPasswordService(
        tokenService = tokenService,
        userService = userService,
        resetPasswordTokenEntityRepository = resetPasswordTokenEntityRepository
    )

    private val mockUser = mock<UserEntity>()
    private val tokenId: Long = 1
    private val tokenString = "test-token"
    private val tokenDate = Date()

    @Test
    @DisplayName("Should save token info in database")
    fun shouldSaveTokenInfoInDatabase() {
        // when
        whenever(tokenService.generateToken()).thenReturn(tokenString)
        whenever(tokenService.calculateExpirationDate()).thenReturn(tokenDate)

        whenever(resetPasswordTokenEntityRepository.save(any<ResetPasswordTokenEntity>())).thenAnswer { invocation ->
            val argument = invocation.getArgument<ResetPasswordTokenEntity>(0)
            assertAll(
                { assertEquals(mockUser, argument.user) },
                { assertEquals(tokenString, argument.token) },
                { assertEquals(tokenDate, argument.expirationDate) }
            )
            argument.id = tokenId
            argument
        }

        // when
        val token = resetPasswordService.generateAndSaveResetToken(mockUser)

        // then
        assertEquals(token, tokenString)
        verify(resetPasswordTokenEntityRepository, times(1)).save(any<ResetPasswordTokenEntity>())
    }


    @Test
    @DisplayName("Should remove existing token and save new token in database")
    fun shouldRemoveExistingAndSaveNewTokenInformationInDatabase() {
        // Given
        val existingToken = ResetPasswordTokenEntity()
        existingToken.id = tokenId
        existingToken.token = "existing-token"

        whenever(tokenService.generateToken()).thenReturn(tokenString)
        whenever(tokenService.calculateExpirationDate()).thenReturn(tokenDate)
        whenever(resetPasswordTokenEntityRepository.findByUser(mockUser)).thenReturn(existingToken)

        whenever(resetPasswordTokenEntityRepository.save(any<ResetPasswordTokenEntity>())).thenAnswer { invocation ->
            val argument = invocation.getArgument<ResetPasswordTokenEntity>(0)
            assertAll(
                { assertEquals(mockUser, argument.user) },
                { assertEquals(tokenString, argument.token) },
                { assertEquals(tokenDate, argument.expirationDate) }
            )
            argument.id = tokenId
            argument
        }

        // When
        val token = resetPasswordService.generateAndSaveResetToken(mockUser)

        // Then
        verify(resetPasswordTokenEntityRepository, times(1)).findByUser(mockUser)
        verify(resetPasswordTokenEntityRepository, times(1)).delete(existingToken)

        assertEquals(token, tokenString)
    }

    @Test
    @DisplayName("Should throw exception when reset password token is not found")
    fun shouldThrowException_whenResetPasswordTokenNotFound() {
        // given
        val token = "token"
        whenever(resetPasswordTokenEntityRepository.findByToken(token)).thenReturn(null)

        // when
        assertThrows<DokyInvalidTokenException> {
            resetPasswordService.validateToken(tokenString)
        }
    }

    @Test
    @DisplayName("Should throw exception when reset password token is expired")
    fun shouldThrowException_whenResetPasswordTokenExpired() {
        // given
        val token = "token"
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.expirationDate = Date(System.currentTimeMillis() - 30_000)
        }
        whenever(resetPasswordTokenEntityRepository.findByToken(token)).thenReturn(resetPasswordTokenEntity)

        // when
        assertThrows<DokyInvalidTokenException> {
            resetPasswordService.validateToken(token)
        }
    }

    @Test
    @DisplayName("Should return reset password token entity when token is valid")
    fun shouldReturnResetPasswordTokenEntity_whenTokenIsValid() {
        // given
        val token = "token"
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.expirationDate = Date(System.currentTimeMillis() + 300_000)
        }
        whenever(resetPasswordTokenEntityRepository.findByToken(token)).thenReturn(resetPasswordTokenEntity)

        // when
        val actualToken = resetPasswordService.validateToken(token)

        // then
        assertNotNull(actualToken)
        assertEquals(actualToken, resetPasswordTokenEntity)
    }
}
