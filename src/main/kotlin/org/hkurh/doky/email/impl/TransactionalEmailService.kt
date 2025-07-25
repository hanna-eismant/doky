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


package org.hkurh.doky.email.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.email.EmailSender
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.mask
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.db.UserEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionalEmailService(
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository,
    private val userEntityRepository: UserEntityRepository,
    @Suppress("SpringJavaInjectionPointsAutowiringInspection") private val emailSender: EmailSender
) : EmailService {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun sendResetPasswordEmail(userId: Long) {
        val tokens = resetPasswordTokenEntityRepository.findValidTokensByUserId(userId)

        if (tokens.isEmpty()) {
            log.warn { "No valid reset password tokens found for user [$userId]" }
        }

        tokens.forEach {
            sendEmailIfNotAlreadySent(
                sentFlag = it.sentEmail,
                logMessage = "Reset Password email was already sent to user [${it.user.id}] for token [${it.token.mask()}]"
            ) {
                emailSender.sendRestorePasswordEmail(it.user, it.token)
                it.sentEmail = true
                resetPasswordTokenEntityRepository.save(it)
            }
        }
    }

    @Transactional
    override fun sendRegistrationEmail(userId: Long) {
        userEntityRepository.findById(userId).ifPresentOrElse(
            { user ->
                sendEmailIfNotAlreadySent(
                    sentFlag = user.sentRegistrationEmail,
                    logMessage = "Registration Confirmation email was already sent to user [$userId]"
                ) {
                    emailSender.sendRegistrationConfirmationEmail(user)
                    user.sentRegistrationEmail = true
                    userEntityRepository.save(user)
                }
            },
            { log.warn { "User [$userId] not found" } }
        )
    }

    fun sendEmailIfNotAlreadySent(sentFlag: Boolean, logMessage: String, action: () -> Unit) {
        if (!sentFlag) {
            action()
        } else {
            log.warn { logMessage }
        }
    }
}
