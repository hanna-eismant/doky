package org.hkurh.doky.password.api

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class UpdatePasswordRequest {
    @Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    lateinit var password: String

    @NotBlank(message = "Token is required")
    lateinit var token: String
}
