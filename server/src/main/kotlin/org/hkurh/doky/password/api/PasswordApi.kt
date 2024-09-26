package org.hkurh.doky.password.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Password")
@SecurityRequirement(name = "Bearer Token")
interface PasswordApi {


    @Operation(summary = "Send a request (email) to restore password for user")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Reset password request is applied successfully")
    )
    fun reset(@RequestBody resetPasswordRequest: ResetPasswordRequest): ResponseEntity<Any>
}
