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

package org.hkurh.doky.kafka

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailSender
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import java.util.*


@DisplayName("KafkaEmailNotificationConsumerService unit test")
class KafkaEmailNotificationConsumerServiceTest : DokyUnitTest {

    private val userEntityRepository: UserEntityRepository = mock()
    private val emailService: EmailService = mock()
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository = mock()
    private val emailSender: EmailSender = mock()

    private val service =
        KafkaEmailNotificationConsumerService(
            userEntityRepository = userEntityRepository,
            resetPasswordTokenEntityRepository = resetPasswordTokenEntityRepository,
            emailService = emailService,
            emailSender = emailSender
        )


    @Test
    @DisplayName("Should send registration email when user exists")
    fun shouldSendRegistrationEmail() {
        // given
        val existingUserId = 16L
        val user = createUser(existingUserId)
        whenever(userEntityRepository.findById(existingUserId)).thenReturn(Optional.ofNullable(user))

        val message = SendEmailMessage().apply {
            userId = existingUserId
            emailType = EmailType.REGISTRATION
        }

        // when
        service.listen(message)

        // then
        verify(emailSender, times(1)).sendRegistrationConfirmationEmail(user)
    }

    @Test
    @DisplayName("Should send reset password email when user and reset token exist")
    fun shouldSendResetPasswordEmail() {
        // given
        val existingUserId = 14L
        val token = "<TOKEN>"
        val user = createUser(existingUserId)
        val resetToken = createResetToken(user, token)
        whenever(userEntityRepository.findById(existingUserId)).thenReturn(Optional.ofNullable(user))
        whenever(resetPasswordTokenEntityRepository.findValidUnsentTokensByUserId(existingUserId)).thenReturn(
            listOf(
                resetToken
            )
        )

        val message = SendEmailMessage().apply {
            userId = existingUserId
            emailType = EmailType.RESET_PASSWORD
        }

        // when
        service.listen(message)

        // then
        verify(emailService, times(1)).sendResetPasswordEmail(resetToken)
    }

    private fun createUser(id: Long): UserEntity {
        return UserEntity().apply {
            this.id = id
            uid = "test@mail.com"
        }
    }

    private fun createResetToken(user: UserEntity, token: String): ResetPasswordTokenEntity {
        return ResetPasswordTokenEntity().apply {
            this.user = user
            this.token = token
        }
    }
}
