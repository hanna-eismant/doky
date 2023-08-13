package org.hkurh.doky.security

import org.hkurh.doky.users.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DokyUserDetails : UserDetails {
    private var username: String = ""
    private var password: String = ""
    private var grantedAuthorities: Collection<GrantedAuthority?>? = null
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return grantedAuthorities!!
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        fun createUserDetails(userEntity: UserEntity): DokyUserDetails {
            val dokyUserDetails = DokyUserDetails()
            dokyUserDetails.username = userEntity.uid!!
            dokyUserDetails.password = userEntity.password!!
            dokyUserDetails.grantedAuthorities = listOf(DokyAuthority.ROLE_USER)
            return dokyUserDetails
        }
    }
}
