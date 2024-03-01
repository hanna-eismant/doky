package org.hkurh.doky.security

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpringSecurityAuditorAware: AuditorAware<UserEntity> {

    override fun getCurrentAuditor(): Optional<UserEntity> {
        val userEntity =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()
        return Optional.ofNullable(userEntity)
    }
}
