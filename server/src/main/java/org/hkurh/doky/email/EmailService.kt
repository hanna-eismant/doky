package org.hkurh.doky.email

import org.hkurh.doky.users.db.UserEntity

/**
 * EmailService interface provides methods for sending emails.
 */
interface EmailService {
    /**
     * Sends a registration confirmation email to the specified user.
     *
     * @param user The user entity for which the registration confirmation email will be sent.
     */
    fun sendRegistrationConfirmationEmail(user: UserEntity)

    /**
     * Sends a restore password email to the specified user with the given token.
     *
     * @param user The user entity for which the restore password email will be sent.
     * @param token The token used for restoring the user's password.
     */
    fun sendRestorePasswordEmail(user: UserEntity, token: String)
}
