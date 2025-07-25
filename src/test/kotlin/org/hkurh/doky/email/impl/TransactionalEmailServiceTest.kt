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


package org.hkurh.doky.email.impl

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailSender
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.util.Optional.empty
import java.util.Optional.ofNullable

@DisplayName("TransactionalEmailService unit test")
class TransactionalEmailServiceTest : DokyUnitTest {

    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository = mock()
    private val userEntityRepository: UserEntityRepository = mock()
    private val emailSender: EmailSender = mock()

    private val emailService = TransactionalEmailService(
        resetPasswordTokenEntityRepository,
        userEntityRepository,
        emailSender
    )

    @Test
    @DisplayName("Should send registration confirmation email if it is not sent")
    fun shouldSendRegistrationConfirmationEmail_ifItIsNotSent() {
        // given
        val existingUserId = 16L
        val user = createUser(existingUserId)
        whenever(userEntityRepository.findById(existingUserId)).thenReturn(ofNullable(user))

        // when
        emailService.sendRegistrationEmail(existingUserId)

        // then
        verify(emailSender).sendRegistrationConfirmationEmail(user)
        argumentCaptor<UserEntity> {
            verify(userEntityRepository).save(capture())
            val captured = firstValue
            assertAll(
                { assertEquals(user.id, captured.id) },
                { assertTrue(captured.sentRegistrationEmail) }
            )
        }
    }

    @Test
    @DisplayName("Should not send registration confirmation email if it is sent")
    fun shouldNotSendRegistrationConfirmationEmail_ifItIsSent() {
        // given
        val existingUserId = 17L
        val user = createUser(existingUserId, true)
        whenever(userEntityRepository.findById(existingUserId)).thenReturn(ofNullable(user))

        // when
        emailService.sendRegistrationEmail(existingUserId)

        // then
        verifyNoInteractions(emailSender)
        verify(userEntityRepository, times(0)).save(any())
    }

    @Test
    @DisplayName("Should not send registration confirmation email if user not found")
    fun shouldNotSendRegistrationConfirmationEmail_ifUserNotFound() {
        // given
        val nonExistingUserId = 18L
        whenever(userEntityRepository.findById(nonExistingUserId)).thenReturn(empty())

        // when
        emailService.sendRegistrationEmail(nonExistingUserId)

        // then
        verifyNoInteractions(emailSender)
        verify(userEntityRepository, times(0)).save(any())
    }

    @Test
    @DisplayName("Should send reset password email if it is not sent")
    fun shouldSendResetPasswordEmail_ifItIsNotSent() {
        // given
        val userId = 19L
        val token = "token"
        val user = createUser(userId)
        val resetToken = createResetToken(user, token)
        whenever(resetPasswordTokenEntityRepository.findValidTokensByUserId(userId)).thenReturn(listOf(resetToken))

        // when
        emailService.sendResetPasswordEmail(userId)

        // then
        verify(emailSender).sendRestorePasswordEmail(user, token)
        argumentCaptor<ResetPasswordTokenEntity> {
            verify(resetPasswordTokenEntityRepository).save(capture())
            val captured = firstValue
            assertAll(
                { assertEquals(user.id, captured.user.id) },
                { assertEquals(user.uid, captured.user.uid) },
                { assertEquals(token, captured.token) },
                { assertTrue(captured.sentEmail) }
            )
        }
    }

    @Test
    @DisplayName("Should not send reset password email if it is sent")
    fun shouldNotSendResetPasswordEmail_ifItIsSent() {
        // given
        val userId = 26L
        val token = "token"
        val user = createUser(userId)
        val resetToken = createResetToken(user, token, true)
        whenever(resetPasswordTokenEntityRepository.findValidTokensByUserId(userId)).thenReturn(listOf(resetToken))

        // when
        emailService.sendResetPasswordEmail(userId)

        // then
        verifyNoInteractions(emailSender)
        verify(resetPasswordTokenEntityRepository, times(0)).save(any())
    }

    @Test
    @DisplayName("Should not send reset password email if no valid tokens found")
    fun shouldNotSendResetPasswordEmail_ifNoValidTokensFound() {
        // given
        val userId = 27L
        whenever(resetPasswordTokenEntityRepository.findValidTokensByUserId(userId)).thenReturn(emptyList())

        // when
        emailService.sendResetPasswordEmail(userId)

        // then
        verifyNoInteractions(emailSender)
        verify(resetPasswordTokenEntityRepository, times(0)).save(any())
    }

    private fun createUser(id: Long, sent: Boolean = false): UserEntity {
        return UserEntity().also {
            it.id = id
            it.uid = "test@mail.com"
            it.sentRegistrationEmail = sent
        }
    }

    private fun createResetToken(user: UserEntity, token: String, sent: Boolean = false): ResetPasswordTokenEntity {
        return ResetPasswordTokenEntity().also {
            it.user = user
            it.token = token
            it.sentEmail = sent
        }
    }
}
