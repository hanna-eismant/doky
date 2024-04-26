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
}
