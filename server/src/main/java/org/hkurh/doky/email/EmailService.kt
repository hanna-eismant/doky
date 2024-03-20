package org.hkurh.doky.email

import jakarta.mail.MessagingException
import org.hkurh.doky.users.db.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
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
    val fromEmail: String? = null

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
            fromEmail?.let { setFrom(it) }
            setTo(user.uid)
            setSubject("Doky Registration")
            setText(htmlBody, true)
        }
        emailSender.send(message)
    }

//    @Throws(MessagingException::class)
//    private fun sendHtmlMessage(to: String, subject: String, htmlBody: String) {
//        val message = emailSender.createMimeMessage()
//        val helper = MimeMessageHelper(message, true, "UTF-8")
//        helper.setFrom(fromEmail)
//        helper.setTo(to)
//        helper.setSubject(subject)
//        helper.setText(htmlBody, true)
//        helper.addInline("attachment.png", resourceFile)
//        emailSender.send(message)
//    }
}
