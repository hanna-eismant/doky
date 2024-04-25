package org.hkurh.doky.password

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@Component
class TokenUtil {

    private val zoneId = ZoneId.of("UTC")

    @Value("\${doky.password.reset.token.duration}")
    var resetTokenDuration: Long = 10

    fun calculateExpirationDate(): Date {
        val currentDate = LocalDateTime.ofInstant(Instant.now(), zoneId)
        val expiredDate = currentDate.plusMinutes(resetTokenDuration)
        return Date.from(expiredDate.toInstant(ZoneOffset.UTC))
    }

    fun generateToken() : String {
        return UUID.randomUUID().toString()
    }
}
