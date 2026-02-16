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

import org.hkurh.doky.kafka.dto.DocumentIndexMessage

/**
 * Service for consuming document indexing messages from a Kafka topic.
 *
 * This functional interface defines the contract for processing document indexing
 * messages received from a Kafka topic. Implementations of this service are expected
 * to handle the deserialization and processing logic for the incoming messages, which
 * contain information about documents that need to be indexed.
 */
fun interface KafkaDocumentIndexConsumerService {

    /**
     * Processes an incoming document indexing message from a Kafka topic.
     *
     * @param message The message containing information about the document to be indexed,
     *                such as its unique identifier.
     */
    fun listen(message: DocumentIndexMessage)
}
