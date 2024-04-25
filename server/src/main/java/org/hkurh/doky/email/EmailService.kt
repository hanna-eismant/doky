package org.hkurh.doky.email

import org.hkurh.doky.users.db.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.nio.charset.StandardCharsets


@Service
class EmailService(
    val emailSender: JavaMailSender,
    val templateEngine: SpringTemplateEngine
) {

    @Value("\${doky.app.host}")
    lateinit var host: String

    @Value("\${doky.email.from}")
    lateinit var fromEmail: String

    @Value("classpath:/mail/img/logo-white-no-bg.svg")
    lateinit var logoFile: Resource

    fun sendRegistrationConfirmationEmail(user: UserEntity) {
        val htmlBody = prepareRegistrationConfirmationEmail(user)
        sendEmail(htmlBody, user.uid, "Doky Registration")
    }

    fun sendRestorePasswordEmail(user: UserEntity, token: String) {
        val htmlBody = prepareRestorePasswordEmail(user, token)
        sendEmail(htmlBody, user.uid, "Doky Restore Password")
    }

    private fun sendEmail(emailTemplate: String, toEmail: String, subject: String) {
        val message = emailSender.createMimeMessage()
        MimeMessageHelper(
            message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        ).apply {
            setFrom(fromEmail)
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
            put("mailto", fromEmail)
        }
        val context = Context().apply {
            setVariables(variables)
        }
        return templateEngine.process(template, context)
    }
}
