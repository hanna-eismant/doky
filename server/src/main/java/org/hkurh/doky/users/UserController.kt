package org.hkurh.doky.users

import org.hkurh.doky.security.DokyAuthority
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Secured(DokyAuthority.Role.ROLE_USER)
class UserController(private val userFacade: UserFacade) : UserApi {

    @get:GetMapping("/users/current")
    override val user: UserDto?
        get() = userFacade.currentUser
}
