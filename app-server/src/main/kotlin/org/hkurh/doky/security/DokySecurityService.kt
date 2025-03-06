package org.hkurh.doky.security

import org.hkurh.doky.users.db.UserEntity

/**
 * The [DokySecurityService] interface defines methods to manage security-related operations
 * for the currently authenticated user within the application.
 */
interface DokySecurityService {

    /**
     * Returns the currently logged-in user.
     *
     * @return The [UserEntity] object representing the currently logged-in user.
     */
    fun getCurrentUser(): UserEntity


    /**
     * Retrieves the unique identifier of the currently logged-in user.
     *
     * @return The identifier of the logged-in user as a string.
     */
    fun getCurrentUserId(): String

    /**
     * Checks if the current user is anonymous.
     *
     * @return True if the current user is not authenticated, otherwise false.
     */
    fun isAnonymous(): Boolean
}
