/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
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

import jakarta.validation.Valid
import org.hkurh.doky.password.PasswordFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * This class is responsible for handling password-related operations through RESTful endpoints.
 *
 * @param passwordFacade The [PasswordFacade] instance used to delegate the password operations.
 */
@RestController
@RequestMapping("/api/password")
class PasswordController(
    private val passwordFacade: PasswordFacade
) : PasswordApi {

    @PostMapping("/reset")
    override fun reset(@RequestBody @Valid resetPasswordRequest: ResetPasswordRequest): ResponseEntity<Any> {
        passwordFacade.reset(resetPasswordRequest.email)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/update")
    override fun update(@RequestBody @Valid updatePasswordRequest: UpdatePasswordRequest): ResponseEntity<Any> {
        passwordFacade.update(updatePasswordRequest.password, updatePasswordRequest.token)
        return ResponseEntity.noContent().build()
    }
}
