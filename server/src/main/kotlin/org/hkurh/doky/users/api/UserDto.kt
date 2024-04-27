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
