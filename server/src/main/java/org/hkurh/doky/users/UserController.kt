package org.hkurh.doky.users

import org.hkurh.doky.security.DokyAuthority
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Secured(DokyAuthority.Role.ROLE_USER)
class UserController(private val userFacade: UserFacade) : UserApi {

    @GetMapping("/users/current")
    override fun getUser(): UserDto {
        return userFacade.getCurrentUser()
    }

    @PutMapping("/users/current")
    override fun updateUser(@Validated @RequestBody userDto: UserDto): ResponseEntity<*> {
        userFacade.updateCurrentUser(userDto)
        return ResponseEntity.noContent().build<Any>()
    }
}
