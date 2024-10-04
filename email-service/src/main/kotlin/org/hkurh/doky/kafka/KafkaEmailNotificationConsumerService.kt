package org.hkurh.doky.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.db.UserEntityRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class KafkaEmailNotificationConsumerService(
    private val userEntityRepository: UserEntityRepository,
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository,
    private val emailService: EmailService
) {
    @KafkaListener(
        id = "\${doky.kafka.emails.consumer.id}",
        topics = ["\${doky.kafka.emails.topic}"],
        groupId = "\${doky.kafka.emails.group.id}",
        containerFactory = "kafkaListenerContainerFactory",
        autoStartup = "true",
        concurrency = "\${doky.kafka.emails.concurrency}"
    )
    fun listen(@Payload message: SendEmailMessage) {
        try {
            LOG.debug { "Received message: $message" }
            message.userId.let {
                when (message.emailType) {
                    EmailType.REGISTRATION -> sendRegistrationEmail(message.userId!!)
                    EmailType.RESET_PASSWORD -> sendResetPasswordEmail(message.userId!!)
                    null -> LOG.warn { "No email type specified" }
                }
            }
        } catch (e: Exception) {
            LOG.error(e) { "Error processing message: $message" }
            throw e
        }
    }

    private fun sendRegistrationEmail(userId: Long) {
        userEntityRepository.findById(userId).ifPresent { user ->
            emailService.sendRegistrationConfirmationEmail(user)
        }
    }

    private fun sendResetPasswordEmail(userId: Long) {
        userEntityRepository.findById(userId).ifPresent { user ->
            resetPasswordTokenEntityRepository.findByUser(user)?.apply {
                this.token?.let { emailService.sendRestorePasswordEmail(user, it) }
            }
        }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
