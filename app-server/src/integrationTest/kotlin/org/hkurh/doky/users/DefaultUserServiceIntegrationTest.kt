package org.hkurh.doky.users

import org.apache.kafka.clients.consumer.Consumer
import org.awaitility.Awaitility.await
import org.hkurh.doky.DokyIntegrationTest
import org.hkurh.doky.users.impl.DefaultUserService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.jdbc.Sql
import java.time.Duration.ofSeconds
import java.util.concurrent.TimeUnit

@DisplayName("DefaultUserService integration test")
class DefaultUserServiceIntegrationTest : DokyIntegrationTest() {

    @Autowired
    lateinit var userService: DefaultUserService

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @Autowired
    lateinit var messageConsumer: Consumer<String, Any>

    private val topic = "emails-test"

    @Disabled("Enable when configure mocking Azure AI Search ")
    @Test
    @DisplayName("Should sent email notification when register new user")
    @Sql(
        scripts = ["classpath:sql/UserServiceIntegrationTest/cleanup.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun shouldSendEmailNotification_whenRegisterNewUser() {
        // given
        val userEmail = "hkurh.test.01@yopmail.com"
        messageConsumer.subscribe(listOf(topic))
        Thread.sleep(3_000)

        // when
        userService.create(userEmail, "password")

        // then
        await().atMost(1, TimeUnit.MINUTES).untilAsserted {
            val message = KafkaTestUtils.getSingleRecord(messageConsumer, topic, ofSeconds(10))
            assertNotNull(message, "No messages received")
        }
        messageConsumer.close()
    }
}
