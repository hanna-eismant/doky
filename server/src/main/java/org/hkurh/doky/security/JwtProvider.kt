package org.hkurh.doky.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hkurh.doky.DokyApplication
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
object JwtProvider {
    private val LOG = LoggerFactory.getLogger(JwtProvider::class.java)
    private val jwtParser = Jwts.parserBuilder().setSigningKey(DokyApplication.SECRET_KEY_SPEC).build()

    fun generateToken(username: String): String {
        LOG.debug("Generate token for user $username")
        val currentTime = DateTime(DateTimeZone.getDefault())
        val expireTokenTime = currentTime.plusDays(1)
        return Jwts.builder()
                .setId("dokyToken")
                .setSubject(username)
                .setIssuedAt(currentTime.toDate())
                .setExpiration(expireTokenTime.toDate())
                .signWith(DokyApplication.SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
                .compact()
    }

    fun getUsernameFromToken(token: String): String {
        return jwtParser.parseClaimsJws(token).body.subject
    }
}
