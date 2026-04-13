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

package org.hkurh.doky.security.impl

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hkurh.doky.security.JwtProvider
import org.hkurh.doky.toUtcDate
import org.hkurh.doky.users.db.UserEntity
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.*
import javax.crypto.spec.SecretKeySpec


@Component
class DefaultJwtProvider(
    private val secretKeySpec: SecretKeySpec,
    private val clock: Clock
) : JwtProvider {

    private val jwtParser = Jwts.parserBuilder().setSigningKey(secretKeySpec).build()
    private val idKey = "token_id"
    private val usernameKey = "username"
    private val rolesKey = "roles"
    private val authTokenId = "dokyAuthToken"
    private val downloadTokenId = "dokyDownloadToken"

    override fun generateToken(username: String, roles: Set<Any>): String {
        val currentTime = LocalDateTime.now(clock)
        val expireTokenTime = currentTime.plusDays(1)
        val claims = mapOf(
            idKey to authTokenId,
            usernameKey to username,
            rolesKey to roles
        )
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentTime.toUtcDate())
            .setExpiration(expireTokenTime.toUtcDate())
            .signWith(secretKeySpec, SignatureAlgorithm.HS256)
            .compact()
    }

    override fun generateDownloadToken(user: UserEntity): String {
        val currentTime = LocalDateTime.now(clock)
        val expireTokenTime = currentTime.plusMinutes(10)
        val claims = mapOf(
            idKey to downloadTokenId
        )
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentTime.toUtcDate())
            .setExpiration(expireTokenTime.toUtcDate())
            .signWith(secretKeySpec, SignatureAlgorithm.HS256)
            .compact()
    }

    override fun getUsernameFromToken(token: String): String {
        return jwtParser.parseClaimsJws(token).body[usernameKey].toString()
    }

    override fun isDownloadTokenValid(token: String): Boolean {
        val claims = jwtParser.parseClaimsJws(token).body
        if (claims[idKey] != downloadTokenId) return false
        val expiration = claims.expiration
        return expiration.after(Date())
    }
}
