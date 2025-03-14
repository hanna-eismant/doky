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

package org.hkurh.doky.users.api

import org.hkurh.doky.users.UserFacade
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * This class represents the [UserController] in the application. It is responsible for handling HTTP requests related to user operations.
 *
 * @param userFacade The [UserFacade] instance for handling user-related business logic.
 */
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ROLE_USER')")
class UserController(private val userFacade: UserFacade) : UserApi {

    @GetMapping("/current")
    override fun getUser(): UserDto {
        return userFacade.getCurrentUser()
    }

    @PutMapping("/current")
    override fun updateUser(@Validated @RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<*> {
        userFacade.updateCurrentUser(updateUserRequest)
        return ResponseEntity.ok().build<Any>()
    }
}
