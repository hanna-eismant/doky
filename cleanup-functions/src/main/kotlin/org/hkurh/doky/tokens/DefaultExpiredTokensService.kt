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

package org.hkurh.doky.tokens

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.password.db.ResetPasswordTokenEntityRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultExpiredTokensService(
    private val resetPasswordTokenEntityRepository: ResetPasswordTokenEntityRepository
) : ExpiredTokensService {

    override fun clearPasswordResetTokens() {
        val currentDate = Date()
        val expiredTokens = resetPasswordTokenEntityRepository.findByExpirationDateLessThan(currentDate)
        resetPasswordTokenEntityRepository.deleteAll(expiredTokens)
        LOG.info { "Cleaned up [${expiredTokens.size}] expired tokens" }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
