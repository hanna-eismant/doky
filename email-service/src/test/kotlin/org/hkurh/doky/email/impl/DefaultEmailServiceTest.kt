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


@DisplayName("DefaultEmailService unit test")
class DefaultEmailServiceTest : DokyUnitTest {

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
    private val defaultEmailService = DefaultEmailService(emailProperties, host, logoFile, emailSender, templateEngine)
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
