package org.hkurh.doky.users

import org.springframework.lang.NonNull

interface UserService {
    fun findUserByUid(@NonNull userUid: String?): UserEntity?
    fun checkUserExistence(@NonNull userUid: String?): Boolean
    fun create(@NonNull userUid: String?, @NonNull encodedPassword: String?): UserEntity?

    @get:NonNull
    val currentUser: UserEntity?
}
