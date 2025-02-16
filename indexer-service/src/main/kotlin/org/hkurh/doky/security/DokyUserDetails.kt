package org.hkurh.doky.security

import org.hkurh.doky.users.db.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DokyUserDetails() : UserDetails {
    private var userEntity: UserEntity? = null
    private var username: String = ""
    private var password: String = ""
    private var grantedAuthorities: Collection<GrantedAuthority?>? = null


    constructor(userEntity: UserEntity) : this() {
        setUserEntity(userEntity)
    }

    private fun setUserEntity(userEntity: UserEntity?) {
        this.userEntity = userEntity
        userEntity?.let { user ->
            this.username = user.uid
            this.password = user.password
            this.grantedAuthorities = user.authorities.map { SimpleGrantedAuthority(it.authority.name) }
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> = grantedAuthorities!!
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
    fun getUserEntity(): UserEntity? = userEntity
}
