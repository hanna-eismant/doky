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

package org.hkurh.doky.users.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hkurh.doky.errorhandling.DokyAuthenticationException
import org.hkurh.doky.errorhandling.DokyRegistrationException
import org.hkurh.doky.toDto
import org.hkurh.doky.users.UserFacade
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.api.UpdateUserRequest
import org.hkurh.doky.users.api.UserDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DefaultUserFacade(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) : UserFacade {

    private val log = KotlinLogging.logger {}

    override fun checkCredentials(userUid: String, password: String): UserDto {
        log.debug { "Checking credentials for user [$userUid]" }
        if (!userService.exists(userUid)) throw DokyAuthenticationException("User not found")
        val userEntity = userService.findUserByUid(userUid)
        val encodedPassword = userEntity.password
        if (!passwordEncoder.matches(password, encodedPassword)) {
            log.warn { "Incorrect credentials for user [$userUid]" }
            throw DokyAuthenticationException("Incorrect credentials")
        }
        return userEntity.toDto()
    }

    override fun register(userUid: String, password: String): UserDto {
        if (userService.exists(userUid)) throw DokyRegistrationException("User already exists")
        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = userService.create(userUid, encodedPassword)
        log.info { "Register new user [${userEntity.id}]" }
        return userEntity.toDto()
    }

    override fun getCurrentUser(): UserDto {
        log.debug { "Get current user info" }
        return userService.getCurrentUser().toDto()
    }

    override fun updateCurrentUser(updateUserRequest: UpdateUserRequest) {
        val currentUser = userService.getCurrentUser()
        currentUser.name = updateUserRequest.name?.takeIf { it.isNotEmpty() } ?: currentUser.name
        userService.updateUser(currentUser)
        log.debug { "User is updated [${currentUser.id}]" }
    }
}
