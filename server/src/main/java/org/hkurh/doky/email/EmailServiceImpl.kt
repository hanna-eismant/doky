package org.hkurh.doky.email

import org.hkurh.doky.users.db.UserEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(private val emailSender: JavaMailSender) : EmailService {
    private final val FROM_EMAIL = "noreply@doky.org"

    override fun sendRegistrationConfirmationEmail(user: UserEntity) {
        val message = SimpleMailMessage()
        message.from = FROM_EMAIL
        message.setTo(user.uid)
        message.subject = "Doky Registration"
        message.text = "Welcome! You registered a new user in Doky app."
        emailSender.send(message)
    }
}
