package org.hkurh.doky.users

import com.icegreen.greenmail.util.GreenMail
import org.awaitility.Awaitility.await
import org.hkurh.doky.DokyIntegrationTest
import org.hkurh.doky.SmtpServerExtension
import org.hkurh.doky.users.impl.DefaultUserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import java.util.concurrent.TimeUnit

@ExtendWith(SmtpServerExtension::class)
@DisplayName("DefaultUserService integration test")
class DefaultUserServiceIntegrationTest : DokyIntegrationTest() {

    @Autowired
    lateinit var userService: DefaultUserService

    var smtpServer: GreenMail? = null

    @Test
    @DisplayName("Should sent registration email when register new user")
    @Sql(
        scripts = ["classpath:sql/UserServiceIntegrationTest/cleanup.sql"],
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    fun shouldSendRegistrationEmail_whenRegisterNewUser() {
        // given
        val userEmail = "hkurh.test.01@yopmail.com"

        // when
        userService.create(userEmail, "password")

        // then
        await().atMost(10, TimeUnit.SECONDS).until { smtpServer!!.receivedMessages.isNotEmpty() }
        smtpServer!!.apply {
            assertNotNull(receivedMessages, "No emails sent")
            assertEquals(1, receivedMessages.size, "Incorrect amount of messages")
            val message = receivedMessages[0]
            assertEquals(userEmail, message.allRecipients[0].toString())
        }
    }
}
