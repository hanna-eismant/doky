package org.hkurh.doky.authorization

import jakarta.validation.Valid
import org.hkurh.doky.security.AuthenticationResponse
import org.hkurh.doky.security.JwtProvider.generateToken
import org.hkurh.doky.users.UserFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class AuthorizationUserController(private val userFacade: UserFacade) : AuthorizationUserApi {
    @PostMapping("/login")
    override fun login(@Valid @RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> {
        val username = authenticationRequest.username
        val password = authenticationRequest.password
        userFacade.checkCredentials(username, password)
        val token = generateToken(username)
        return ResponseEntity.ok(AuthenticationResponse(token))
    }

    @PostMapping(value = ["/register"], consumes = ["application/json"])
    override fun register(@Valid @RequestBody registrationRequest: AuthenticationRequest): ResponseEntity<*>? {
        val registeredUser = userFacade.register(registrationRequest.username, registrationRequest.password)
        val resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").build(registeredUser!!.userUid)
        return ResponseEntity.created(resourceLocation).build<Any>()
    }
}