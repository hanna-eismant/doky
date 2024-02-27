package org.hkurh.doky.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class UserDto {
    var id: Long? = null
    @field:Email
    var uid: String? = null
    var name: String? = null
    @field:Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @field:Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    var password: String? = null
}
