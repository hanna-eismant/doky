package org.hkurh.doky.users

import org.hkurh.doky.errorhandling.DokyAuthenticationException
import org.springframework.lang.NonNull

interface UserFacade {
    /**
     * Check if provided credentials are correct.
     *
     * @throws DokyAuthenticationException if user with provided username does not exist,
     * or provided password is incorrect
     */
    fun checkCredentials(@NonNull userUid: String?, @NonNull password: String?)
    fun register(@NonNull username: String?, @NonNull password: String?): UserDto?
    val currentUser: UserDto?
}
