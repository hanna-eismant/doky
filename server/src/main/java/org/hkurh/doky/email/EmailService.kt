package org.hkurh.doky.email

import org.hkurh.doky.users.UserEntity

interface EmailService {
    fun sendRegistrationConfirmationEmail(user: UserEntity)
}
