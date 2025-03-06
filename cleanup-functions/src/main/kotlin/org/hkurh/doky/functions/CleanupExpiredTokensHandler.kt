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

package org.hkurh.doky.functions

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.FixedDelayRetry
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.TimerTrigger
import org.hkurh.doky.tokens.ExpiredTokensService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class CleanupExpiredTokensHandler {

    @Autowired
    lateinit var expiredTokensService: ExpiredTokensService

    @FunctionName("CleanupExpiredTokens")
    @FixedDelayRetry(maxRetryCount = 4, delayInterval = "00:10:00")
    fun execute(
        @TimerTrigger(name = "CleanupExpiredTokensTrigger", schedule = "0 0 3 * * *") timerInfo: String?,
        context: ExecutionContext?
    ) {
        expiredTokensService.clearPasswordResetTokens()
    }
}
