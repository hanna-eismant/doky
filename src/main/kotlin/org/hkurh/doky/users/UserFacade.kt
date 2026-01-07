/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

package org.hkurh.doky.users

import org.hkurh.doky.users.api.UpdateUserRequest
import org.hkurh.doky.users.api.UserDto

/**
 * The [UserFacade] interface represents the facade for user-related operations.
 */
interface UserFacade {
    /**
     * Checks the credentials of a user.
     *
     * @param userUid The user's unique ID.
     * @param password The user's password.
     */
    fun checkCredentials(userUid: String, password: String): UserDto

    /**
     * Registers a new user with the given user UID and password.
     *
     * @param userUid The user's unique ID.
     * @param password The user's password.
     * @return The registered user as a [UserDto] object.
     */
    fun register(userUid: String, password: String): UserDto

    /**
     * Retrieves the current user that is authenticated for request.
     *
     * @return The current user as a [UserDto] object.
     */
    fun getCurrentUser(): UserDto

    /**
     * Updates the information of the current user that is authenticated for request.
     *
     * @param updateUserRequest The updated user information as a [UpdateUserRequest] object.
     */
    fun updateCurrentUser(updateUserRequest: UpdateUserRequest)
}
