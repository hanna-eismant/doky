package org.hkurh.doky.email

import org.hkurh.doky.users.db.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine


@Service
class EmailService(
    val emailSender: JavaMailSender,
    val templateEngine: SpringTemplateEngine
) {

    @Value("\${doky.email.from}")
    lateinit var fromEmail: String

    @Value("classpath:/mail/img/logo-white-no-bg.svg")
    lateinit var logoFile: Resource

    fun sendRegistrationConfirmationEmail(user: UserEntity) {
        val template = "registration.html"
        val variables = HashMap<String, Any>().apply {
            user.name?.let { put("username", it) }
        }
        val context = Context().apply {
            setVariables(variables)
        }
        val htmlBody: String = templateEngine.process(template, context)
        val message = emailSender.createMimeMessage()
        MimeMessageHelper(message, true, "UTF-8").apply {
            setFrom(fromEmail)
            setTo(user.uid)
            setSubject("Doky Registration")
            setText(htmlBody, true)
            addInline("logo-white-no-bg.svg", logoFile)
        }
        emailSender.send(message)
    }
}
