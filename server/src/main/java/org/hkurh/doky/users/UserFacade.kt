package org.hkurh.doky.users

import org.hkurh.doky.users.api.UserDto

interface UserFacade {
    fun checkCredentials(userUid: String, password: String)
    fun register(userUid: String, password: String): UserDto
    fun getCurrentUser(): UserDto
    fun updateCurrentUser(userDto: UserDto)
}
