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

package org.hkurh.doky.kafka.impl

import org.hkurh.doky.kafka.KafkaEmailNotificationProducerService
import org.hkurh.doky.kafka.dto.EmailType
import org.hkurh.doky.kafka.dto.SendEmailMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Implementation of the [KafkaEmailNotificationProducerService] that sends email notification messages to
 * a specified Kafka topic.
 *
 * This service is responsible for producing email notification messages, associating them with a unique key
 * derived from the user ID, email type, and current timestamp, and sending them to the configured Kafka topic.
 *
 * @constructor Initializes the service with the Kafka topic name and [KafkaTemplate] instance required for message production.
 * @param topic The name of the Kafka topic where email notification messages will be sent. This is injected
 *              from the application configuration.
 * @param kafkaTemplate The [KafkaTemplate] used for sending messages to Kafka.
 */
@Service
class DefaultKafkaEmailNotificationProducerService(
    @Value("\${doky.kafka.emails.topic}") topic: String = "",
    kafkaTemplate: KafkaTemplate<String, Any>
) : AbstractProducerService(topic, kafkaTemplate), KafkaEmailNotificationProducerService {

    override fun send(userId: Long, emailType: EmailType) {
        val key = "$userId|$emailType|${LocalDateTime.now()}"
        val message = SendEmailMessage().apply {
            this.userId = userId
            this.emailType = emailType
        }
        sendKafkaMessage(key, message)
    }
}
