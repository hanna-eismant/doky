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

package org.hkurh.doky.email.impl

import jakarta.mail.internet.MimeMessage
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailProperties
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import org.thymeleaf.spring6.SpringTemplateEngine


@DisplayName("DefaultEmailSender unit test")
class SmtpEmailSenderTest : DokyUnitTest {

    private val emailProperties = EmailProperties().apply {
        sender = EmailProperties.Sender().apply {
            email = "email"
            name = "name"
        }
        registration = EmailProperties.Registration().apply {
            subject = "subject"
            sendgrid = EmailProperties.Registration.Sendgrid().apply {
                templateId = "templateId"
            }
        }
        resetPassword = EmailProperties.ResetPassword().apply {
            subject = "subject"
            sendgrid = EmailProperties.ResetPassword.Sendgrid().apply {
                templateId = "templateId"
            }
        }
        sendgrid = EmailProperties.Sendgrid().apply {
            apiKey = "apiKey"
        }
    }
    private val host = "http://localhost:8080"
    private val logoFile: Resource = mock()
    private val emailSender: JavaMailSender = mock()
    private val templateEngine: SpringTemplateEngine = mock()
    private val mimeMessage: MimeMessage = mock()
    private val defaultEmailService = SmtpEmailSender(emailProperties, host, logoFile, emailSender, templateEngine)
    private val user = UserEntity().apply {
        name = "John"
        uid = "test@mail.com"
    }

    @BeforeEach
    fun setUp() {
        whenever(logoFile.filename).thenReturn("logo.png")
        val htmlBody = "htmlBody"
        whenever(templateEngine.process(any<String>(), any())).thenReturn(htmlBody)
        whenever(emailSender.createMimeMessage()).thenReturn(mimeMessage)
    }

    @Test
    @DisplayName("Should send registration email")
    fun shouldSendRegistrationEmail() {
        // when
        defaultEmailService.sendRegistrationConfirmationEmail(user)

        // then
        verify(emailSender, times(1)).send(mimeMessage)
    }

    @Test
    @DisplayName("Should send reset password request email")
    fun shouldSendResetPasswordRequestEmail() {
        // given
        val token = "token"

        // when
        defaultEmailService.sendRestorePasswordEmail(user, token)

        // then
        verify(emailSender, times(1)).send(mimeMessage)
    }
}
