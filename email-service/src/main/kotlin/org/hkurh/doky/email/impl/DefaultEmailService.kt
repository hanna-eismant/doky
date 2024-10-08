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
