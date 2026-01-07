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

package org.hkurh.doky.users

import org.apache.kafka.clients.consumer.Consumer
import org.awaitility.Awaitility.await
import org.hkurh.doky.DokyIntegrationTest
import org.hkurh.doky.users.impl.DefaultUserService
import org.junit.jupiter.api.Assertions.assertNotNull
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
