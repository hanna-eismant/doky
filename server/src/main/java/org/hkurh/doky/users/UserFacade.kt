package org.hkurh.doky.users

import org.hkurh.doky.errorhandling.DokyAuthenticationException

interface UserFacade {
    /**
     * Check if provided credentials are correct.
     *
     * @throws DokyAuthenticationException if user with provided username does not exist,
     * or provided password is incorrect
     */
    fun checkCredentials(userUid: String, password: String)
    fun register(username: String, password: String): UserDto
    fun getCurrentUser(): UserDto
}
