package org.hkurh.doky.password.impl

import org.hkurh.doky.password.TokenService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@Component
class DefaultTokenService : TokenService {

    private val zoneId = ZoneId.of("UTC")

    @Value("\${doky.password.reset.token.duration}")
    var resetTokenDuration: Long = 10

    override fun calculateExpirationDate(): Date {
        val currentDate = LocalDateTime.ofInstant(Instant.now(), zoneId)
        val expiredDate = currentDate.plusMinutes(resetTokenDuration)
        return Date.from(expiredDate.toInstant(ZoneOffset.UTC))
    }

    override fun generateToken(): String {
        return UUID.randomUUID().toString()
    }
}
