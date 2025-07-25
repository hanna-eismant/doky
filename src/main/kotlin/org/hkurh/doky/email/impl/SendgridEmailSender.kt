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

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailProperties
import org.hkurh.doky.email.EmailSender
import org.hkurh.doky.errorhandling.DokyEmailException
import org.hkurh.doky.users.db.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service


@Service
@ConditionalOnProperty(name = ["doky.email.server.type"], havingValue = "sendgrid", matchIfMissing = false)
class SendgridEmailSender(
    private val emailProperties: EmailProperties,
    @Value("\${doky.app.host}") private val host: String
) : EmailSender {

    private val log = KotlinLogging.logger {}
    private val defaultUserName = "Customer"

    override fun sendRegistrationConfirmationEmail(user: UserEntity) {
        log.debug { "Sending registration email for user [${user.id}]" }
        sendEmail(
            senderEmail = emailProperties.sender.email,
            senderName = emailProperties.sender.name,
            recipientEmail = user.uid,
            subject = emailProperties.registration.subject,
            templateId = emailProperties.registration.sendgrid.templateId,
            dynamicData = mapOf("User_Name" to (user.name ?: defaultUserName))
        )
    }

    override fun sendRestorePasswordEmail(user: UserEntity, token: String) {
        log.debug { "Sending reset password email for user [${user.id}]" }
        sendEmail(
            senderEmail = emailProperties.sender.email,
            senderName = emailProperties.sender.name,
            recipientEmail = user.uid,
            subject = emailProperties.resetPassword.subject,
            templateId = emailProperties.resetPassword.sendgrid.templateId,
            dynamicData = mapOf(
                "User_Name" to (user.name ?: defaultUserName),
                "Reset_Password_Link" to generateResetPasswordLink(token)
            )
        )
    }

    private fun sendEmail(
        senderEmail: String,
        senderName: String,
        recipientEmail: String,
        subject: String,
        templateId: String,
        dynamicData: Map<String, String>
    ) {
        val sender = Email(senderEmail, senderName)
        val to = Email(recipientEmail)

        val personalization = Personalization().apply {
            dynamicData.forEach { (key, value) -> addDynamicTemplateData(key, value) }
            addTo(to)
        }

        val mail = Mail().apply {
            from = sender
            this.subject = subject
            this.templateId = templateId
            addPersonalization(personalization)
        }

        sendEmailToSendGrid(mail)
    }

    private fun generateResetPasswordLink(token: String): String {
        return "$host/password/update?token=$token"
    }

    fun sendEmailToSendGrid(mail: Mail) {
        val sendGridClient = SendGrid(emailProperties.sendgrid.apiKey)
        val request = Request()
        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()
        val response: Response = sendGridClient.api(request)
        log.debug { "Response code: ${response.statusCode}; body: ${response.body}" }
        if (response.statusCode !in 200..299) {
            throw DokyEmailException("Error during sending email. Response code: ${response.statusCode}; body: ${response.body}")
        }
    }
}
