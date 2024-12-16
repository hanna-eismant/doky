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
