package org.hkurh.doky.password

import org.hkurh.doky.users.db.UserEntity

/**
 * ResetPasswordService interface provides methods for manipulating a reset password token.
 */
interface ResetPasswordService {
    /**
     * Generate and save a reset password token for the specified user.
     *
     * @param user The [UserEntity] for which the reset password token will be generated and saved.
     * @return The generated reset password token.
     */
    fun generateAndSaveResetToken(user: UserEntity): String
}
