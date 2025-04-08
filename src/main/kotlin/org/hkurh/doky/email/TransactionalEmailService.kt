package org.hkurh.doky.email

import org.hkurh.doky.password.db.ResetPasswordTokenEntity

interface TransactionalEmailService {
    fun sendResetPasswordEmail(resetPasswordTokenEntity: ResetPasswordTokenEntity)
}
