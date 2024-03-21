package org.hkurh.doky.users

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetup
import com.icegreen.greenmail.util.ServerSetup.PROTOCOL_SMTP
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@ActiveProfiles("test")
@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("UserService integration test")
class UserServiceIntegrationTest {
    val greenMail = GreenMail(ServerSetup(2525, null, PROTOCOL_SMTP))

    @Autowired
    lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        greenMail.apply {
            withConfiguration(GreenMailConfiguration.aConfig().withDisabledAuthentication())
            start()
        }
    }

    @AfterEach
    fun tearDown() {
        greenMail.stop()
    }

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
        greenMail.apply {
            assertNotNull(receivedMessages, "No emails sent")
            assertEquals(1, receivedMessages.size, "Incorrect amount of messages")
            val message = receivedMessages[0]
            assertEquals(userEmail, message.allRecipients[0].toString())
        }
    }
}
