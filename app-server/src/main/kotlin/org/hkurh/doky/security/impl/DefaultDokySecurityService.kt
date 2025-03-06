package org.hkurh.doky.security.impl

import org.hkurh.doky.errorhandling.DokyAuthenticationException
import org.hkurh.doky.security.DokySecurityService
import org.hkurh.doky.security.DokyUserDetails
import org.hkurh.doky.users.db.UserEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service("dokySecurityService")
class DefaultDokySecurityService : DokySecurityService {

    override fun getCurrentUser(): UserEntity {
        return (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()
            ?: throw DokyAuthenticationException("No user logged in")
    }

    override fun getCurrentUserId(): String {
        return (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity()?.id?.toString()
            ?: "anonymous"
    }

    override fun isAnonymous(): Boolean {
        return (SecurityContextHolder.getContext().authentication.principal as DokyUserDetails).getUserEntity() == null
    }
}
