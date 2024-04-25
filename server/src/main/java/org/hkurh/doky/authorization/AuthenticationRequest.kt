package org.hkurh.doky.authorization

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class AuthenticationRequest {
    @NotBlank(message = "Email is required")
    @Size(min = 4, max = 32, message = "Length should be from 4 to 32 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$",
        message = "Should be an valid email address"
    )
    var uid: String = ""

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    var password: String = ""
}
