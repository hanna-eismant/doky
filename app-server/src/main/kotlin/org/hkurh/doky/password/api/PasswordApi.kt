package org.hkurh.doky.password.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Password")
interface PasswordApi {

    @Operation(summary = "Send a request (email) to restore password for user")
    @ApiResponses(
        ApiResponse(responseCode = "204 No Content", description = "Reset password request is applied successfully")
    )
    fun reset(@RequestBody resetPasswordRequest: ResetPasswordRequest): ResponseEntity<Any>

    @Operation(summary = "Update password using reset password token")
    @ApiResponses(
        ApiResponse(responseCode = "204 No Content", description = "Password has been updated successfully"),
        ApiResponse(responseCode = "400 Bad Request", description = "Invalid token"),
    )
    fun update(@RequestBody updatePasswordRequest: UpdatePasswordRequest): ResponseEntity<Any>
}
