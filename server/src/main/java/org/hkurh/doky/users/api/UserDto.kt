package org.hkurh.doky.users.api

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * This class is a Data Transfer Object class used for transferring user information.
 *
 * @property id The unique ID of the user.
 * @property uid The email of the user. Must be a valid email address.
 * @property name The name of the user.
 * @property password The password of the user.
 */
data class UserDto(
    var id: Long,
    @Email
    var uid: String,
    var name: String? = null,
    @Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    var password: String? = null
)
