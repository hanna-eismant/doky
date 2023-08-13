package org.hkurh.doky.users

import org.hkurh.doky.security.DokyAuthority
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Secured(DokyAuthority.Role.ROLE_USER)
class UserController(private val userFacade: UserFacade) : UserApi {

    @GetMapping("/users/current")
    override fun getUser(): UserDto {
        return userFacade.getCurrentUser()
    }
}
