package org.hkurh.doky.security

import org.hkurh.doky.users.UserEntity
import org.hkurh.doky.users.UserService
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpringSecurityAuditorAware(private val userService: UserService): AuditorAware<UserEntity> {

    override fun getCurrentAuditor(): Optional<UserEntity> = Optional.ofNullable(userService.getCurrentUser())
}
