/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.kafka

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Component
class KafkaEmailNotificationProducerService(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    private val log = KotlinLogging.logger {}

    @Value("\${doky.kafka.emails.topic}")
    private var topic: String = ""

    fun sendNotification(userId: Long, emailType: EmailType) {
        val key = "$userId|$emailType|${LocalDateTime.now()}"
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
                log.debug { "Produced message to topic [${result.recordMetadata.topic()}] with key [$key]" }
            } else {
                log.error(e) { "Error sending email message with key [$key]" }
            }
        }
    }
}
