package org.hkurh.doky.kafka

import com.icegreen.greenmail.util.GreenMail
import org.awaitility.Awaitility.await
import org.hkurh.doky.DokyIntegrationTest
import org.hkurh.doky.SmtpServerExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import java.sql.Types
import java.util.concurrent.TimeUnit


@ExtendWith(SmtpServerExtension::class)
@DisplayName("KafkaEmailNotificationConsumerService integration test")
class KafkaEmailNotificationConsumerServiceIntegrationTest : DokyIntegrationTest() {

    val userEmail = "hanna_test_1@yopmail.com"

    var smtpServer: GreenMail? = null

    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var kafkaEmailNotificationConsumerService: KafkaEmailNotificationConsumerService

    @Test
    @DisplayName("Should send registration email")
    fun shouldSendRegistrationEmail() {
        // given
        val userId = getUserId(userEmail)
        val message = SendEmailMessage().apply {
            this.userId = userId
            emailType = EmailType.REGISTRATION
        }

        // when
        kafkaEmailNotificationConsumerService.listen(message)

        // then
        await().atMost(10, TimeUnit.SECONDS).until { smtpServer!!.receivedMessages.isNotEmpty() }
        smtpServer!!.apply {
            assertNotNull(receivedMessages, "No emails sent")
            assertEquals(1, receivedMessages.size, "Incorrect amount of messages")
            val emailMessage = receivedMessages[0]
            assertEquals(userEmail, emailMessage.allRecipients[0].toString())
        }
    }

    @Test
    @DisplayName("Should send reset password email")
    @Sql(
        scripts = ["classpath:sql/KafkaEmailNotificationConsumerServiceIntegrationTest/setup.sql"],
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    fun shouldSendResetPasswordEmail() {
        // given
        val userId = getUserId(userEmail)
        val message = SendEmailMessage().apply {
            this.userId = userId
            emailType = EmailType.RESET_PASSWORD
        }

        // when
        kafkaEmailNotificationConsumerService.listen(message)

        // then
        await().atMost(10, TimeUnit.SECONDS).until { smtpServer!!.receivedMessages.isNotEmpty() }
        smtpServer!!.apply {
            assertNotNull(receivedMessages, "No emails sent")
            assertEquals(1, receivedMessages.size, "Incorrect amount of messages")
            val emailMessage = receivedMessages[0]
            assertEquals(userEmail, emailMessage.allRecipients[0].toString())
        }
    }

    fun getUserId(userUid: String): Long? {
        val existedUserQuery = "select u.id from users u where u.uid = ?"
        val args = arrayOf(userUid)
        val argTypes = intArrayOf(Types.VARCHAR)
        return jdbcTemplate.queryForObject(existedUserQuery, args, argTypes, Long::class.java)
    }
}