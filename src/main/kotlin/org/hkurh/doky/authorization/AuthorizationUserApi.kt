/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2025
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.authorization

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.hkurh.doky.errorhandling.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "User authorization and registration")
@SecurityRequirement(name = "")
interface AuthorizationUserApi {
    @Operation(summary = "User login")
    @ApiResponses(
            ApiResponse(responseCode = "200", description = "User is logged in successful",
                    content = [Content(schema = Schema(implementation = AuthenticationResponse::class))]),
            ApiResponse(responseCode = "401", description = "Incorrect username or password",
                    content = [Content(schema = Schema(implementation = ErrorResponse::class))]))
    fun login(@Valid @RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse>?

    @Operation(summary = "User registration")
    @ApiResponses(
            ApiResponse(responseCode = "201", description = "User is created",
                    content = [Content(schema = Schema(implementation = AuthenticationResponse::class))]),
            ApiResponse(responseCode = "409", description = "User with same name already exists",
                    content = [Content(schema = Schema(implementation = ErrorResponse::class))]))
    fun register(@Valid @RequestBody registrationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse>?
}
