package org.hkurh.doky.security

import org.hkurh.doky.users.db.UserEntity
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

/**
 * Implementation of [AuditorAware] interface that retrieves the current auditor (user) from Spring Security.
 */
@Component
class SpringSecurityAuditorAware : AuditorAware<UserEntity> {

    /**
     * Retrieves the current auditor (user) from Spring Security.
     *
     * @return An [Optional] containing the [UserEntity] representing the current auditor,
     *         or an empty [Optional] if the auditor is not found.
     */
    override fun getCurrentAuditor(): Optional<UserEntity> {
        val userEntity =
            (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()
        return Optional.ofNullable(userEntity)
    }
}
