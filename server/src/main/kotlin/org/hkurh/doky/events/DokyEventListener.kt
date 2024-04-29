package org.hkurh.doky.events

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailService
import org.springframework.context.event.EventListener
import org.springframework.mail.MailException
import org.springframework.stereotype.Component

@Component
class DokyEventListener(
    private val emailService: EmailService
) {

    @EventListener
    fun sendRegistrationEmail(event: UserRegistrationEvent) {
        try {
            LOG.debug { "Process user registration event for user [${event.user.id}]" }
            emailService.sendRegistrationConfirmationEmail(event.user)
        } catch (e: MailException) {
            LOG.error(e) { "Error during sending registration email for user [${event.user.id}]" }
        }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
