package org.hkurh.doky.users

import org.hkurh.doky.users.db.UserEntity

/**
 * [UserService] interface provides methods for manipulating user data.
 */
interface UserService {
    /**
     * Finds a user by their unique identifier.
     *
     * @param userUid The unique identifier of the user.
     * @return The UserEntity object representing the user, or null if the user is not found.
     */
    fun findUserByUid(userUid: String): UserEntity?

    /**
     * Creates a new user entity with the given user UID and encoded password.
     *
     * @param userUid The unique identifier of the user.
     * @param encodedPassword The encoded password of the user.
     * @return The newly created UserEntity object.
     */
    fun create(userUid: String, encodedPassword: String): UserEntity

    /**
     * Returns the currently logged-in user.
     *
     * @return The [UserEntity] object representing the currently logged-in user.
     */
    fun getCurrentUser(): UserEntity

    /**
     * Updates the information of a user.
     *
     * @param user The [UserEntity] object representing the user to update.
     */
    fun updateUser(user: UserEntity)

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email of the user.
     * @return `true` if a user with the given email exists, `false` otherwise.
     */
    fun exists(email: String): Boolean

    /**
     * Checks if a user with the given ID exists.
     *
     * @param id The ID of the user.
     * @return `true` if a user with the given ID exists, `false` otherwise.
     */
    fun exists(id: Long): Boolean
}
