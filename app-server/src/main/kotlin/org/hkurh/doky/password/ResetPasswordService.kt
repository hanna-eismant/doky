package org.hkurh.doky.password

import org.hkurh.doky.errorhandling.DokyInvalidTokenException
import org.hkurh.doky.password.db.ResetPasswordTokenEntity
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

    /**
     * Checks the validity of the provided token and retrieves the corresponding
     * reset password token entity.
     *
     * @param token The reset password token to be checked.
     * @return The [ResetPasswordTokenEntity] associated with the provided token.
     *
     * @throws [DokyInvalidTokenException] if provided token is not found or expired.
     */
    fun checkToken(token: String): ResetPasswordTokenEntity

    /**
     * Deletes the specified reset password token entity from the system.
     *
     * @param resetPasswordToken The [ResetPasswordTokenEntity] to be deleted.
     */
    fun delete(resetPasswordToken: ResetPasswordTokenEntity)
}
