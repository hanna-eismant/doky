package org.hkurh.doky.users.api

import org.hkurh.doky.users.UserFacade
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * This class represents the [UserController] in the application. It is responsible for handling HTTP requests related to user operations.
 *
 * @param userFacade The [UserFacade] instance for handling user-related business logic.
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
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
