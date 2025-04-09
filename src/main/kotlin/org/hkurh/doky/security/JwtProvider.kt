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

package org.hkurh.doky.security

import org.hkurh.doky.users.db.UserEntity

/**
 * Interface representing a provider for managing JSON Web Tokens (JWT).
 *
 * This interface defines methods for generating, extracting, and validating JWT tokens,
 * as well as functionality to handle specific token types like download tokens.
 */
interface JwtProvider {

    /**
     * Generates a JWT token for the given username and roles.
     *
     * @param username the username for which the token is being generated
     * @param roles a set of roles associated with the user
     * @return a JSON Web Token (JWT) as a string
     */
    fun generateToken(username: String, roles: Set<Any>): String

    /**
     * Generates a JWT download token for the given user.
     *
     * @param user the user entity for which the download token is being generated
     * @return a JWT download token as a string
     */
    fun generateDownloadToken(user: UserEntity): String

    /**
     * Extracts the username from the provided JSON Web Token (JWT).
     *
     * @param token the JSON Web Token (JWT) from which the username will be extracted
     * @return the username embedded within the JWT as a string
     */
    fun getUsernameFromToken(token: String): String

    /**
     * Validates whether the provided download token is valid.
     *
     * @param token the download token to be validated
     * @return true if the token is valid, false otherwise
     */
    fun isDownloadTokenValid(token: String): Boolean
}
