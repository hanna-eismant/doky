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
import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.password.TokenService
import org.hkurh.doky.password.TokenStatus
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service

@Service
class DefaultResetPasswordService(
    private val tokenService: TokenService,
    private val userService: UserService,
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository,
) : ResetPasswordService {

    private val log = KotlinLogging.logger {}

    override fun generateAndSaveResetToken(user: UserEntity): String {
        val token = tokenService.generateToken()
        val expirationDate = tokenService.calculateExpirationDate()
        val resetPasswordTokenEntity = ResetPasswordTokenEntity().apply {
            this.token = token
            this.user = user
            this.expirationDate = expirationDate
        }
        val savedPasswordTokenEntity = resetPasswordTokenEntityRepository.save(resetPasswordTokenEntity)
        log.info { "Generated reset token for user [${user.id}]" }
        return savedPasswordTokenEntity.token
    }

    override fun validateToken(token: String): TokenStatus {
        val resetToken = resetPasswordTokenEntityRepository.findByToken(token)
        log.debug { "Validating token: $token" }
        resetToken?.let {
            return when {
                isTokenExpired(it) -> {
                    log.warn { "Token expired: $token" }
                    TokenStatus.EXPIRED
                }

                !isTokenBelongsToCurrentUser(it) -> {
                    log.warn { "Token does not belong to the current user: $token" }
                    TokenStatus.INVALID
                }

                else -> TokenStatus.VALID
            }
        }
        log.warn { "Token invalid or not found: $token" }
        return TokenStatus.INVALID
    }

    override fun delete(token: String) {
        resetPasswordTokenEntityRepository.deleteByToken(token)
    }

    private fun isTokenBelongsToCurrentUser(token: ResetPasswordTokenEntity): Boolean {
        val currentUser = userService.getCurrentUser()
        return token.user.id == currentUser.id
    }

    private fun isTokenExpired(token: ResetPasswordTokenEntity) =
        token.expirationDate.toInstant().isBefore(java.time.Instant.now())
}
