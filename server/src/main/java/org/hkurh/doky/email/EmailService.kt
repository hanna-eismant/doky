package org.hkurh.doky.email

import org.hkurh.doky.users.db.UserEntity

interface EmailService {
    fun sendRegistrationConfirmationEmail(user: UserEntity)
}
