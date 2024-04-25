package org.hkurh.doky.password.api

import jakarta.validation.Valid
import org.hkurh.doky.password.PasswordFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/password")
class PasswordController(
    val passwordFacade: PasswordFacade
) : PasswordApi {

    @PostMapping("/reset")
    override fun reset(@RequestBody @Valid resetPasswordRequest: ResetPasswordRequest): ResponseEntity<Any> {
        passwordFacade.reset(resetPasswordRequest.email)
        return ResponseEntity.noContent().build()
    }
}
