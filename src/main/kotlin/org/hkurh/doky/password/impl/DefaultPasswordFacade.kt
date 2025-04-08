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
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.password.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.errorhandling.DokyInvalidTokenException
import org.hkurh.doky.kafka.EmailType
import org.hkurh.doky.kafka.KafkaEmailNotificationProducerService
import org.hkurh.doky.password.PasswordFacade
import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.password.TokenStatus
import org.hkurh.doky.users.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


@Component
class DefaultPasswordFacade(
    private val userService: UserService,
    private val resetPasswordService: ResetPasswordService,
    private val kafkaEmailNotificationProducerService: KafkaEmailNotificationProducerService,
    private val passwordEncoder: PasswordEncoder,
) : PasswordFacade {

    private val log = KotlinLogging.logger {}

    override fun reset(email: String) {
        if (!userService.exists(email)) {
            log.debug { "Requested reset password token for non existing user" }
            return
        }

        val user = userService.findUserByUid(email)
        resetPasswordService.generateAndSaveResetToken(user)
        log.debug { "Generate reset password token for user [${user.id}]" }
        kafkaEmailNotificationProducerService.sendNotification(user.id, EmailType.RESET_PASSWORD)
    }

    override fun update(password: String, token: String) {
        val tokenStatus = resetPasswordService.validateToken(token)
        if (tokenStatus == TokenStatus.VALID) {
            val updatedPassword = passwordEncoder.encode(password)
            userService.getCurrentUser().apply {
                this.password = updatedPassword
                userService.updateUser(this)
            }
            resetPasswordService.delete(token)
        } else {
            throw DokyInvalidTokenException("Invalid token [$token] due to [${tokenStatus.message}]")
        }
    }
}
