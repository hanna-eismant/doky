/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailSender
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.db.UserEntityRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class KafkaEmailNotificationConsumerService(
    private val userEntityRepository: UserEntityRepository,
    private val emailService: EmailService,
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository,
    private val emailSender: EmailSender
) {

    private val log = KotlinLogging.logger {}

    @KafkaListener(
        id = "\${doky.kafka.emails.consumer.id}",
        topics = ["\${doky.kafka.emails.topic}"],
        groupId = "\${doky.kafka.emails.group.id}",
        containerFactory = "kafkaListenerContainerFactory",
        autoStartup = "true",
        concurrency = "\${doky.kafka.emails.concurrency}"
    )
    fun listen(@Payload message: SendEmailMessage) {
        try {
            log.debug { "Received message: [$message]" }
            message.userId.let {
                when (message.emailType) {
                    EmailType.REGISTRATION -> sendRegistrationEmail(message.userId!!)
                    EmailType.RESET_PASSWORD -> sendResetPasswordEmail(message.userId!!)
                    null -> log.warn { "No email type specified" }
                }
            }
        } catch (e: Exception) {
            log.error(e) { "Error processing message: [$message]" }
        }
    }

    private fun sendRegistrationEmail(userId: Long) {
        userEntityRepository.findById(userId).ifPresent { user ->
            emailSender.sendRegistrationConfirmationEmail(user)
        }
    }

    private fun sendResetPasswordEmail(userId: Long) {
        resetPasswordTokenEntityRepository.findValidUnsentTokensByUserId(userId)
            .forEach {
                try {
                    emailService.sendResetPasswordEmail(it)
                } catch (e: Exception) {
                    log.error(e) { "Error sending reset password email for user [$userId]" }
                }
            }
    }
}
