package org.hkurh.doky.password

import java.util.*

interface TokenService {
    fun calculateExpirationDate(): Date
    fun generateToken(): String
}
