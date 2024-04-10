package org.hkurh.doky.users.api

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hkurh.doky.security.DokyAuthority
import org.hkurh.doky.users.UserFacade
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

data class UserDto (
    var id: Long,
    @Email
    var uid: String,
    var name: String? = null,
    @Size(min = 8, max = 32, message = "Length should be from 8 to 32 characters")
    @Pattern(regexp = "^[a-zA-Z\\d!@#$%^&*()_\\-+]*$")
    var password: String? = null
)
