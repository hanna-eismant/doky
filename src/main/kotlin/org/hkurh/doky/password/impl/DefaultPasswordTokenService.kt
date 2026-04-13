/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.password.impl

import org.hkurh.doky.password.PasswordTokenService
import org.hkurh.doky.toUtcDate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@Component
class DefaultPasswordTokenService(
    @set:Value("\${doky.password.reset.token.duration}") private var resetTokenDuration: Long = 10,
    private val clock: Clock
) : PasswordTokenService {

    override fun calculateExpirationDate(): Date = LocalDateTime.now(clock).plusMinutes(resetTokenDuration).toUtcDate()

    override fun generateToken(): String = UUID.randomUUID().toString()
}
