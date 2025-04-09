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
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.email

import org.hkurh.doky.users.db.UserEntity

/**
 * EmailService interface provides methods for sending emails.
 */
interface EmailSender {
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
