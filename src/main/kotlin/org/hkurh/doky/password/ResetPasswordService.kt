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
     * Validates the provided reset password token and determines its status.
     *
     * @param token The reset password token to validate.
     * @return The status of the token, which can be one of [TokenStatus.VALID], [TokenStatus.EXPIRED], or [TokenStatus.INVALID].
     */
    fun validateToken(token: String): TokenStatus

    /**
     * Deletes the reset password token associated with the specified token value.
     *
     * @param token The reset password token that needs to be deleted.
     */
    fun delete(token: String)
}
