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
