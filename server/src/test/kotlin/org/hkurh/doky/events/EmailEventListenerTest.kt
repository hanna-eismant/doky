package org.hkurh.doky.events

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.events.listeners.EmailEventListener
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mail.MailSendException

@DisplayName("EmailEventListenerTest unit test")
class EmailEventListenerTest : DokyUnitTest {

    private val emailService: EmailService = mock()
    private val eventListener = EmailEventListener(emailService)

    @Test
    @DisplayName("Should send an email")
    fun shouldSendAnEmail() {
        // given
        val user = UserEntity()
        val event = UserRegistrationEvent(this, user)

        // when
        eventListener.sendRegistrationEmail(event)

        // then
        verify(emailService).sendRegistrationConfirmationEmail(user)
    }

    @Test
    @DisplayName("Should not fail if sending email fails")
    fun shouldNotFail_whenSendingEmailFails() {
        // given
        val user = UserEntity()
        val event = UserRegistrationEvent(this, user)
        whenever(emailService.sendRegistrationConfirmationEmail(user)).thenThrow(MailSendException("Test exception"))

        // when - then
        assertDoesNotThrow { eventListener.sendRegistrationEmail(event) }
    }
}
