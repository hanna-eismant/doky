package org.hkurh.doky.users

interface UserService {
    fun findUserByUid(userUid: String): UserEntity?
    fun checkUserExistence(userUid: String): Boolean
    fun create(userUid: String, encodedPassword: String): UserEntity
    fun getCurrentUser(): UserEntity
    fun updateUser(user: UserEntity)
}
