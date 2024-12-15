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
        LOG.debug { "Cleaned up [${expiredTokens.size}] expired tokens" }
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
