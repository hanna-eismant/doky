/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
