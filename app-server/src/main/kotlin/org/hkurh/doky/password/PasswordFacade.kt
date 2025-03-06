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

/**
 * The PasswordFacade interface provides methods for handling password-related operations.
 */
interface PasswordFacade {
    /**
     * Resets the password for the user with the specified email.
     *
     * @param email The email of the user whose password needs to be reset.
     */
    fun reset(email: String)

    /**
     * Updates the password using the provided token.
     *
     * @param password The new password to set.
     * @param token The token used to validate the password reset request.
     */
    fun update(password: String, token: String)
}
