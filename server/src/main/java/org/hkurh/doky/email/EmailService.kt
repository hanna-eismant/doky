package org.hkurh.doky.email

import org.hkurh.doky.users.db.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val emailSender: JavaMailSender) {

    @Value("\${doky.email.from}")
    val fromEmail: String? = null

    fun sendRegistrationConfirmationEmail(user: UserEntity) {
        val message = SimpleMailMessage()
        message.from = fromEmail
        message.setTo(user.uid)
        message.subject = "Doky Registration"
        message.text = "Welcome! You registered a new user in Doky app."
        emailSender.send(message)
    }
}
