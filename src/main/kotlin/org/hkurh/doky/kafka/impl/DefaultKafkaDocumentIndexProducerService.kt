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

import org.hkurh.doky.kafka.KafkaDocumentIndexProducerService
import org.hkurh.doky.kafka.dto.DocumentIndexMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Implementation of the [KafkaDocumentIndexProducerService] for sending document indexing
 * messages to a specified Kafka topic.
 *
 * This service uses a [KafkaTemplate] to produce messages to a Kafka topic defined in configuration.
 * Each message is uniquely keyed to include the document ID and the current timestamp. The service
 * ensures asynchronous message production and provides logging for success or failure cases.
 *
 * @constructor Initializes the service with the Kafka topic name and [KafkaTemplate] instance required for message production.
 * @param topic The Kafka topic to which document indexing messages are sent, retrieved from application configuration.
 * @param kafkaTemplate The [KafkaTemplate] used for sending messages to the Kafka topic.
 */
@Service
class DefaultKafkaDocumentIndexProducerService(
    @Value("\${doky.kafka.documents.topic}") topic: String = "",
    kafkaTemplate: KafkaTemplate<String, Any>
) : AbstractProducerService(topic, kafkaTemplate), KafkaDocumentIndexProducerService {

    override fun send(documentId: Long) {
        val key = "$documentId|${LocalDateTime.now()}"
        val message = DocumentIndexMessage().apply { this.documentId = documentId }
        sendKafkaMessage(key, message)
    }
}
