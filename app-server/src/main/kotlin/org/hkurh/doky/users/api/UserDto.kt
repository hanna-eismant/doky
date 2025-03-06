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

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * This class is a Data Transfer Object class used for transferring user information.
 *
 * @property id The unique ID of the user.
 * @property uid The email of the user. Must be a valid email address.
 * @property name The name of the user.
 * @property password The password of the user.
 * @property roles The list of roles assigned to user. Is not added to response.
 */
class UserDto {
    var id: Long = 0
    var uid: String = ""
    var name: String? = null

    @Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    var password: String? = null

    @JsonIgnore
    var roles: MutableSet<String> = mutableSetOf()
}
