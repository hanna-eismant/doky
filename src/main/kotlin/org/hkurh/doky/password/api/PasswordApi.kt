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
