package org.hkurh.doky.authorization

import jakarta.validation.Valid
import org.hkurh.doky.security.JwtProvider.generateToken
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
class AuthorizationUserController(private val userFacade: UserFacade) : AuthorizationUserApi {
    @PostMapping("/login")
    override fun login(@Valid @RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val username = authenticationRequest.uid
        val password = authenticationRequest.password
        val user = userFacade.checkCredentials(username, password)
        val token = generateToken(user.uid, user.roles)
        return ResponseEntity.ok(AuthenticationResponse(token))
    }

    @PostMapping(value = ["/register"], consumes = ["application/json"])
    override fun register(@Valid @RequestBody registrationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse>? {
        val registeredUser = userFacade.register(registrationRequest.uid, registrationRequest.password)
        val resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").build(registeredUser.id)
        val token = generateToken(registeredUser.uid, registeredUser.roles)
        return ResponseEntity.created(resourceLocation).body(AuthenticationResponse(token))
    }
}
