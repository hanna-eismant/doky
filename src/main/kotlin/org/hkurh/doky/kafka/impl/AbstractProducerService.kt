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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.concurrent.CompletableFuture

/**
 * Abstract service for Kafka message production.
 *
 * This class serves as a base for implementing Kafka producers that send messages to a specified topic.
 * It uses a [KafkaTemplate] to facilitate asynchronous message production and ensures proper logging
 * for both successful and failed message delivery.
 *
 * @constructor Creates an instance of the abstract producer service.
 * @param topic The Kafka topic to which the messages are sent.
 * @param kafkaTemplate The [KafkaTemplate] instance used for sending messages to Kafka.
 */
abstract class AbstractProducerService(
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val log = KotlinLogging.logger {}

    protected fun sendKafkaMessage(key: String, message: Any) {
        val future: CompletableFuture<SendResult<String, Any>> = kafkaTemplate.send(topic, key, message)
        future.whenComplete { _, e ->
            if (e == null) {
                log.debug { "Produced message to topic [$topic] with key [$key]" }
            } else {
                log.error(e) { "Error sending message to topic [$topic] with key [$key]" }
            }
        }
    }
}
