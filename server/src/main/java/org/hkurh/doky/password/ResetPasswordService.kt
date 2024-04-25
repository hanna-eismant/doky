package org.hkurh.doky.password

import org.hkurh.doky.users.db.UserEntity

interface ResetPasswordService {
    fun generateAndSaveResetToken(user: UserEntity): String
}
