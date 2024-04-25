package org.hkurh.doky.users

import org.hkurh.doky.users.db.UserEntity

interface UserService {
    fun findUserByUid(userUid: String): UserEntity?
    fun create(userUid: String, encodedPassword: String): UserEntity
    fun getCurrentUser(): UserEntity
    fun updateUser(user: UserEntity)
    fun exists(email: String): Boolean
    fun exists(id: Long): Boolean
}
