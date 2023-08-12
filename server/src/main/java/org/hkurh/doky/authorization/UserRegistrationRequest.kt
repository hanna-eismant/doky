package org.hkurh.doky.authorization

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


data class AuthenticationRequest(
        @field:NotBlank(message = "Username is required")
        @field:Size(min = 4, max = 32, message = "Length should be from 4 to 32 characters")
        @field:Pattern(regexp = "^[a-zA-Z\\d_\\-]*$")
        var username: String,

        @field:NotBlank(message = "Password is required")
        @field:Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
        @field:Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
        var password: String
)
