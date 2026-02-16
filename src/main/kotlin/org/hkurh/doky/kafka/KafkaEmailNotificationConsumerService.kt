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

import org.hkurh.doky.kafka.dto.SendEmailMessage

/**
 * Service for consuming email notification messages from a Kafka topic.
 *
 * This interface defines the contract for processing email notification messages
 * received from a Kafka topic. Implementations of this service are expected to
 * handle the deserialization and processing logic for the incoming messages, which
 * typically contain details such as user identifiers and email types.
 */
fun interface KafkaEmailNotificationConsumerService {

    /**
     * Processes an incoming email notification message from a Kafka topic.
     *
     * @param message The message containing the user identifier and email type
     *                information for sending an email notification.
     */
    fun listen(message: SendEmailMessage)
}
