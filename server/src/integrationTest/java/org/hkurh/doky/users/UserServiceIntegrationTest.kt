package org.hkurh.doky.users

import com.icegreen.greenmail.util.GreenMail
import org.hkurh.doky.DokyIntegrationTest
import org.hkurh.doky.SmtpServerExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

@ExtendWith(SmtpServerExtension::class)
@DisplayName("UserService integration test")
class UserServiceIntegrationTest : DokyIntegrationTest() {

    @Autowired
    lateinit var userService: UserService

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
        smtpServer!!.apply {
            assertNotNull(receivedMessages, "No emails sent")
            assertEquals(1, receivedMessages.size, "Incorrect amount of messages")
            val message = receivedMessages[0]
            assertEquals(userEmail, message.allRecipients[0].toString())
        }
    }
}
