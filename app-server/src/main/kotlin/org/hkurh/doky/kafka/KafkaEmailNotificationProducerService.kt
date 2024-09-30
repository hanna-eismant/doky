package org.hkurh.doky.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class KafkaEmailNotificationProducerService(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    @Value("\${doky.kafka.topic.emails}")
    private var topic: String = ""

    fun sendNotification(userId: Long, emailType: EmailType) {
        val key = "$userId"
        val message = SendEmailMessage().apply {
            this.userId = userId
            this.emailType = emailType
        }
        sendKafkaMessage(key, message)
    }

    private fun sendKafkaMessage(key: String, message: SendEmailMessage) {
        val future: CompletableFuture<SendResult<String, Any>> = kafkaTemplate.send(topic, key, message)
        future.whenComplete { result, e ->
            if (e == null) {
                LOG.debug { "Produced message to topic [${result.recordMetadata.topic()}] with key [$key]" }
            } else {
                LOG.error(e) { "Error sending reset password email message with key [$key]" }
            }
        }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
