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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailProperties
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.users.db.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.nio.charset.StandardCharsets


@Service
@ConditionalOnProperty(name = ["doky.email.server.type"], havingValue = "smtp", matchIfMissing = true)
class DefaultEmailService(
    val emailProperties: EmailProperties,
    @Value("\${doky.app.host}") val host: String,
    @Value("classpath:/mail/img/logo-white-no-bg.svg") val logoFile: Resource,
    val emailSender: JavaMailSender,
    val templateEngine: SpringTemplateEngine
) : EmailService {

    override fun sendRegistrationConfirmationEmail(user: UserEntity) {
        LOG.debug { "Send registration email for user [${user.id}]" }
        val htmlBody = prepareRegistrationConfirmationEmail(user)
        sendEmail(htmlBody, user.uid, emailProperties.registration.subject)
    }

    override fun sendRestorePasswordEmail(user: UserEntity, token: String) {
        LOG.debug { "Send reset password email for user [${user.id}]" }
        val htmlBody = prepareRestorePasswordEmail(user, token)
        sendEmail(htmlBody, user.uid, emailProperties.resetPassword.subject)
    }

    private fun sendEmail(emailTemplate: String, toEmail: String, subject: String) {
        val message = emailSender.createMimeMessage()
        MimeMessageHelper(
            message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        ).apply {
            setFrom(emailProperties.sender.email, emailProperties.sender.name)
            setTo(toEmail)
            setSubject(subject)
            setText(emailTemplate, true)
            addInline("logo-white-no-bg.svg", logoFile)
        }
        emailSender.send(message)
    }

    private fun prepareRegistrationConfirmationEmail(user: UserEntity): String {
        val template = "registration.html"
        val variables = HashMap<String, Any>().apply {
            user.name?.let { put("username", it) }
        }
        val context = Context().apply {
            setVariables(variables)
        }
        return templateEngine.process(template, context)
    }

    private fun prepareRestorePasswordEmail(user: UserEntity, token: String): String {
        val template = "restore-password.html"
        val variables = HashMap<String, Any>().apply {
            user.name?.let { put("username", it) }
            put("restoreLink", "$host/password/update?token=$token")
            put("mailto", emailProperties.sender.email)
        }
        val context = Context().apply {
            setVariables(variables)
        }
        return templateEngine.process(template, context)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
