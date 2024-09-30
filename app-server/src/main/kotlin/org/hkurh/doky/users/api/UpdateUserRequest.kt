package org.hkurh.doky.users.api

import jakarta.validation.constraints.NotBlank

class UpdateUserRequest {
    @NotBlank(message = "User name cannot be empty")
    var name: String? = null
}
