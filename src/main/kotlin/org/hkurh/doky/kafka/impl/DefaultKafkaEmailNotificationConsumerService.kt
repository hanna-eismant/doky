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

package org.hkurh.doky.kafka.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.kafka.KafkaEmailNotificationConsumerService
import org.hkurh.doky.kafka.dto.EmailType
import org.hkurh.doky.kafka.dto.SendEmailMessage
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class DefaultKafkaEmailNotificationConsumerService(
    private val emailService: EmailService
) : KafkaEmailNotificationConsumerService {

    private val log = KotlinLogging.logger {}

    @KafkaListener(
        id = "\${doky.kafka.emails.consumer.id}",
        topics = ["\${doky.kafka.emails.topic}"],
        groupId = "\${doky.kafka.emails.group.id}",
        containerFactory = "emailNotificationKafkaListenerContainerFactory",
        autoStartup = "true",
        concurrency = "\${doky.kafka.emails.concurrency}"
    )
    override fun listen(@Payload message: SendEmailMessage) {
        log.debug { "Received message: [$message]" }
        message.userId.let {
            when (message.emailType) {
                EmailType.REGISTRATION -> emailService.sendRegistrationEmail(message.userId!!)
                EmailType.RESET_PASSWORD -> emailService.sendResetPasswordEmail(message.userId!!)
                null -> log.warn { "No email type specified" }
            }
        }
    }
}
