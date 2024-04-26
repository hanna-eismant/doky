package org.hkurh.doky.security

import org.springframework.security.core.GrantedAuthority

enum class DokyAuthority(private val authority: String) : GrantedAuthority {
    ROLE_USER(Role.ROLE_USER);

    override fun getAuthority(): String {
        return authority
    }

    object Role {
        const val ROLE_USER = "ROLE_USER"
    }
}
