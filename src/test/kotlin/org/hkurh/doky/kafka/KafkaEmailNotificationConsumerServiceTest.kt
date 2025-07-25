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
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock


@DisplayName("KafkaEmailNotificationConsumerService unit test")
class KafkaEmailNotificationConsumerServiceTest : DokyUnitTest {

    private val emailService: EmailService = mock()

    private val service = KafkaEmailNotificationConsumerService(emailService)


    @Test
    @DisplayName("Should send registration email when user exists")
    fun shouldSendRegistrationEmail() {
        // given
        val existingUserId = 16L
        val user = createUser(existingUserId)

        val message = SendEmailMessage().apply {
            userId = existingUserId
            emailType = EmailType.REGISTRATION
        }

        // when
        service.listen(message)

        // then
        verify(emailService).sendRegistrationEmail(user.id)
    }

    @Test
    @DisplayName("Should send reset password email when user and reset token exist")
    fun shouldSendResetPasswordEmail() {
        // given
        val existingUserId = 14L
        val user = createUser(existingUserId)

        val message = SendEmailMessage().apply {
            userId = existingUserId
            emailType = EmailType.RESET_PASSWORD
        }

        // when
        service.listen(message)

        // then
        verify(emailService).sendResetPasswordEmail(user.id)
    }

    private fun createUser(id: Long): UserEntity {
        return UserEntity().apply {
            this.id = id
            uid = "test@mail.com"
        }
    }
}
