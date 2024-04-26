package org.hkurh.doky.password.api

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * Represents a request to reset a password.
 */
class ResetPasswordRequest {
    @NotBlank(message = "Email is required")
    @Size(min = 4, max = 32, message = "Length should be from 4 to 32 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$",
        message = "Should be an valid email address"
    )
    var email: String = ""
}
