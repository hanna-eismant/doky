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

/**
 * Service for producing document indexing messages to a Kafka topic.
 *
 * This interface defines the contract for sending document indexing messages
 * based on a unique document identifier. Implementations of this service are
 * responsible for serializing and sending the messages to the appropriate
 * Kafka topic for further processing by consumers.
 */
interface KafkaDocumentIndexProducerService {

    /**
     * Sends a document indexing message to a Kafka topic for the specified document.
     *
     * @param documentId The unique identifier of the document to be indexed.
     */
    fun send(documentId: Long)
}
