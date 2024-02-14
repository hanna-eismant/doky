package org.hkurh.doky.security

import org.hkurh.doky.users.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DokyUserDetails : UserDetails {
    private var userEntity: UserEntity? = null
    private var username: String = ""
    private var password: String = ""
    private var grantedAuthorities: Collection<GrantedAuthority?>? = null

    override fun getAuthorities(): Collection<GrantedAuthority?> = grantedAuthorities!!
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
    fun getUserEntity(): UserEntity? = userEntity

    companion object {
        fun createUserDetails(userEntity: UserEntity): DokyUserDetails {
            val dokyUserDetails = DokyUserDetails()
            dokyUserDetails.userEntity = userEntity
            dokyUserDetails.username = userEntity.uid
            dokyUserDetails.password = userEntity.password
            dokyUserDetails.grantedAuthorities = listOf(DokyAuthority.ROLE_USER)
            return dokyUserDetails
        }
    }
}
