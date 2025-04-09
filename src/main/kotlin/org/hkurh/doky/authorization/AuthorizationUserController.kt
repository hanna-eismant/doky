/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
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

import jakarta.validation.Valid
import org.hkurh.doky.security.JwtProvider

import org.hkurh.doky.users.UserFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

/**
 * Represents a controller for handling user authorization and registration.
 *
 * @property userFacade The facade for user-related operations.
 */
@RestController
@RequestMapping("/api")
class AuthorizationUserController(
    private val userFacade: UserFacade,
    private val jwtProvider: JwtProvider
) : AuthorizationUserApi {

    @PostMapping("/login")
    override fun login(@Valid @RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val username = authenticationRequest.uid
        val password = authenticationRequest.password
        val user = userFacade.checkCredentials(username, password)
        val token = jwtProvider.generateToken(user.uid, user.roles)
        return ResponseEntity.ok(AuthenticationResponse(token))
    }

    @PostMapping(value = ["/register"], consumes = ["application/json"])
    override fun register(@Valid @RequestBody registrationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse>? {
        val registeredUser = userFacade.register(registrationRequest.uid, registrationRequest.password)
        val resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").build(registeredUser.id)
        val token = jwtProvider.generateToken(registeredUser.uid, registeredUser.roles)
        return ResponseEntity.created(resourceLocation).body(AuthenticationResponse(token))
    }
}
