/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
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

package org.hkurh.doky.password.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.mask
import org.hkurh.doky.password.ResetPasswordService
import org.hkurh.doky.password.TokenService
import org.hkurh.doky.password.TokenStatus
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Service

@Service
class DefaultResetPasswordService(
    private val tokenService: TokenService,
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
        return savedPasswordTokenEntity.token
    }

    override fun validateToken(token: String): TokenStatus {
        val resetToken = resetPasswordTokenEntityRepository.findByToken(token)
        log.debug { "Validating token: [${token.mask()}]" }
        resetToken?.let {
            return when {
                isTokenExpired(it) -> {
                    log.warn { "Token expired for user: [${it.user.id}]" }
                    TokenStatus.EXPIRED
                }

                else -> TokenStatus.VALID
            }
        }
        log.warn { "Token invalid or not found: [${token.mask()}]" }
        return TokenStatus.INVALID
    }

    override fun delete(token: String) {
        resetPasswordTokenEntityRepository.deleteByToken(token)
    }

    override fun getUserForToken(token: String): UserEntity {
        return resetPasswordTokenEntityRepository.findByToken(token)?.user
            ?: throw DokyNotFoundException("User not found for token [${token.mask()}]")
    }

    private fun isTokenExpired(token: ResetPasswordTokenEntity) =
        token.expirationDate.toInstant().isBefore(java.time.Instant.now())
}
