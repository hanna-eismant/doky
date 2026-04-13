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

import org.hkurh.doky.DokyUnitTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@DisplayName("DefaultTokenService unit test")
class DefaultPasswordTokenServiceTest : DokyUnitTest {

    @Test
    @DisplayName("Should calculate expiration date")
    fun shouldCalculateExpirationDate() {
        // given
        val fixedInstant = Instant.parse("2022-01-01T00:00:00.00Z")
        val clock = Clock.fixed(fixedInstant, ZoneId.of("UTC"))
        val tokenService = DefaultPasswordTokenService(3, clock)

        // when
        val actualExpirationDate = tokenService.calculateExpirationDate()

        // then
        val expectedExpirationDate = Date.from(Instant.parse("2022-01-01T00:03:00Z"))
        assertEquals(expectedExpirationDate, actualExpirationDate)
    }
}
