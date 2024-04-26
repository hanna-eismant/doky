package org.hkurh.doky.password

import java.util.*

/**
 * TokenService interface provides methods for generating tokens and calculating their expiration date.
 */
interface TokenService {
    /**
     * Calculates the expiration date for a token.
     *
     * @return The expiration date for the token.
     */
    fun calculateExpirationDate(): Date

    /**
     * Generates a token.
     *
     * @return The generated token as a string.
     */
    fun generateToken(): String
}
