package org.hkurh.doky.kafka

import org.hkurh.doky.DokyUnitTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.concurrent.CompletableFuture

@DisplayName("KafkaEmailNotificationService unit test")
class KafkaEmailNotificationProducerServiceTest : DokyUnitTest {

    private val kafkaTemplate: KafkaTemplate<String, Any> = mock()
    private val kafkaEmailNotificationProducerService = KafkaEmailNotificationProducerService(kafkaTemplate)

    @Test
    @DisplayName("Should send message to kafka")
    fun shouldSendMessageToKafka() {
        // given
        val userId = 16L
        whenever(
            kafkaTemplate.send(
                any<String>(),
                eq(userId.toString()),
                any<SendEmailMessage>()
            )
        ).thenAnswer { invocation ->
            val argument = invocation.getArgument<SendEmailMessage>(2)
            assertEquals(userId, argument.userId)
            assertEquals(EmailType.RESET_PASSWORD, argument.emailType)
            CompletableFuture<SendResult<String, Any>>()
        }

        // when
        kafkaEmailNotificationProducerService.sendNotification(userId, EmailType.RESET_PASSWORD)

        // then
        verify(kafkaTemplate, times(1)).send(any(), eq(userId.toString()), any())

    }
}
