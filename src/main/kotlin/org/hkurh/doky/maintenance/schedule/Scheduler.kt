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

package org.hkurh.doky.maintenance.schedule

/**
 * Provides functionality for scheduled tasks in the application.
 *
 * This interface is intended to define methods for operations that should be executed periodically
 * or according to a specified schedule. Implementing classes should provide the logic for these
 * tasks, leveraging appropriate scheduling mechanisms.
 */
interface Scheduler {

    /**
     * Removes expired reset password tokens from the system.
     *
     * This method is intended to periodically clean up reset password tokens
     * that are no longer valid, ensuring the integrity and security of the
     * password reset functionality. Implementations of this method should
     * identify and delete tokens that have surpassed their expiration period.
     */
    fun cleanupExpiredResetPasswordTokens()
}
