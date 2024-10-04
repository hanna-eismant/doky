package org.hkurh.doky.password

/**
 * The PasswordFacade interface provides methods for handling password-related operations.
 */
interface PasswordFacade {
    /**
     * Resets the password for the user with the specified email.
     *
     * @param email The email of the user whose password needs to be reset.
     */
    fun reset(email: String)

    /**
     * Updates the password using the provided token.
     *
     * @param password The new password to set.
     * @param token The token used to validate the password reset request.
     */
    fun update(password: String, token: String)
}
