package org.hkurh.doky.users.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Users")
@SecurityRequirement(name = "Bearer Token")
interface UserApi {
    @ApiResponses(
            ApiResponse(responseCode = "200", description = "User information is retrieved successfully",
                    content = [Content(schema = Schema(implementation = UserDto::class))]))
    @Operation(summary = "Get current user info")
    fun getUser(): UserDto

    @ApiResponses(
        ApiResponse(responseCode = "201", description = "User information is updated successfully",
            content = [Content(schema = Schema(implementation = UserDto::class))]))
    @Operation(summary = "Update current user info")
    fun updateUser(userDto: UserDto): ResponseEntity<*>?
}
