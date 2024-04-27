package org.hkurh.doky.users

import org.hkurh.doky.users.api.UserDto

/**
 * The [UserFacade] interface represents the facade for user-related operations.
 */
interface UserFacade {
    /**
     * Checks the credentials of a user.
     *
     * @param userUid The user's unique ID.
     * @param password The user's password.
     */
    fun checkCredentials(userUid: String, password: String): UserDto

    /**
     * Registers a new user with the given user UID and password.
     *
     * @param userUid The user's unique ID.
     * @param password The user's password.
     * @return The registered user as a [UserDto] object.
     */
    fun register(userUid: String, password: String): UserDto

    /**
     * Retrieves the current user that is authenticated for request.
     *
     * @return The current user as a [UserDto] object.
     */
    fun getCurrentUser(): UserDto

    /**
     * Updates the information of the current user that is authenticated for request.
     *
     * @param userDto The updated user information as a [UserDto] object.
     */
    fun updateCurrentUser(userDto: UserDto)
}
