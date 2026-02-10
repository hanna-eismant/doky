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

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.kafka.dto.EmailType
import org.hkurh.doky.kafka.dto.SendEmailMessage
import org.hkurh.doky.kafka.impl.DefaultKafkaEmailNotificationProducerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.concurrent.CompletableFuture

@DisplayName("KafkaEmailNotificationService unit test")
class DefaultKafkaEmailNotificationProducerServiceTest : DokyUnitTest {

    private val topic = "test-topic"
    private val kafkaTemplate: KafkaTemplate<String, Any> = mock()
    private val kafkaEmailNotificationProducerService =
        DefaultKafkaEmailNotificationProducerService(topic, kafkaTemplate)

    @Test
    @DisplayName("Should send message to kafka")
    fun shouldSendMessageToKafka() {
        // given
        val userId = 16L
        whenever(
            kafkaTemplate.send(
                eq(topic),
                any<String>(),
                any<SendEmailMessage>()
            )
        ).thenAnswer { invocation ->
            val argument = invocation.getArgument<SendEmailMessage>(2)
            assertEquals(userId, argument.userId)
            assertEquals(EmailType.RESET_PASSWORD, argument.emailType)
            CompletableFuture<SendResult<String, Any>>()
        }

        // when
        kafkaEmailNotificationProducerService.send(userId, EmailType.RESET_PASSWORD)

        // then
        argumentCaptor<SendEmailMessage> {
            verify(kafkaTemplate, times(1)).send(eq(topic), any<String>(), capture())
            val data = firstValue
            assertEquals(userId, data.userId)
            assertEquals(EmailType.RESET_PASSWORD, data.emailType)
        }
    }
}
