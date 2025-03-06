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

import com.sendgrid.helpers.mail.Mail
import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailProperties
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("SendgridEmailService unit test")
class SendgridEmailServiceTest : DokyUnitTest {

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
    private val sendgridEmailService = SendgridEmailService(emailProperties, host)

    @Test
    @DisplayName("Should send registration email")
    fun shouldSendRegistrationEmail() {
        // given
        val user = UserEntity().apply {
            name = "John"
            uid = "test@mail.com"
        }

        val sendgridEmailServiceSpy = spy(sendgridEmailService)
        doNothing().whenever(sendgridEmailServiceSpy).sendEmailToSendGrid(any())

        // when
        sendgridEmailServiceSpy.sendRegistrationConfirmationEmail(user)

        // then
        argumentCaptor<Mail>().apply {
            verify(sendgridEmailServiceSpy).sendEmailToSendGrid(capture())
            val mail = firstValue
            assertAll(
                { assertEquals(emailProperties.registration.subject, mail.subject) },
                { assertEquals(emailProperties.sender.email, mail.from.email) },
                { assertEquals(emailProperties.sender.name, mail.from.name) },
                { assertEquals(mail.personalization[0].tos[0].email, user.uid) },
                { assertEquals(mail.personalization[0].dynamicTemplateData["User_Name"], user.name) }
            )
        }
    }

    @Test
    @DisplayName("Should send reset password request email")
    fun shouldSendResetPasswordRequestEmail() {
        // given
        val user = UserEntity().apply {
            name = "John"
            uid = "test@mail.com"
        }
        val token = "token"
        val sendgridEmailServiceSpy = spy(sendgridEmailService)
        doNothing().whenever(sendgridEmailServiceSpy).sendEmailToSendGrid(any())

        // when
        sendgridEmailServiceSpy.sendRestorePasswordEmail(user, token)

        // then
        argumentCaptor<Mail>().apply {
            verify(sendgridEmailServiceSpy).sendEmailToSendGrid(capture())
            val mail = firstValue
            assertAll(
                { assertEquals(emailProperties.registration.subject, mail.subject) },
                { assertEquals(emailProperties.sender.email, mail.from.email) },
                { assertEquals(emailProperties.sender.name, mail.from.name) },
                { assertEquals(mail.personalization[0].tos[0].email, user.uid) },
                { assertEquals(mail.personalization[0].dynamicTemplateData["User_Name"], user.name) },
                { assertNotNull(mail.personalization[0].dynamicTemplateData["Reset_Password_Link"]) }
            )
        }
    }
}
