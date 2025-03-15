/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
