/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
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

package org.hkurh.doky.users

import org.hkurh.doky.users.db.UserEntity

/**
 * [UserService] interface provides methods for manipulating user data.
 */
interface UserService {
    /**
     * Finds a user by their unique identifier.
     *
     * @param userUid The unique identifier of the user.
     * @return The UserEntity object representing the user.
     */
    fun findUserByUid(userUid: String): UserEntity

    /**
     * Creates a new user entity with the given user UID and encoded password.
     *
     * @param userUid The unique identifier of the user.
     * @param encodedPassword The encoded password of the user.
     * @return The newly created UserEntity object.
     */
    fun create(userUid: String, encodedPassword: String): UserEntity

    /**
     * Returns the currently logged-in user.
     *
     * @return The [UserEntity] object representing the currently logged-in user.
     */
    fun getCurrentUser(): UserEntity

    /**
     * Updates the information of a user.
     *
     * @param user The [UserEntity] object representing the user to update.
     */
    fun updateUser(user: UserEntity)

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email of the user.
     * @return `true` if a user with the given email exists, `false` otherwise.
     */
    fun exists(email: String): Boolean

    /**
     * Checks if a user with the given ID exists.
     *
     * @param id The ID of the user.
     * @return `true` if a user with the given ID exists, `false` otherwise.
     */
    fun exists(id: Long): Boolean
}
