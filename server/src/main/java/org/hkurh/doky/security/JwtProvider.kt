package org.hkurh.doky.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hkurh.doky.DokyApplication
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.slf4j.LoggerFactory
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component

@Component
object JwtProvider {
    private val LOG = LoggerFactory.getLogger(JwtProvider::class.java)
    private val jwtParser = Jwts.parserBuilder().setSigningKey(DokyApplication.SECRET_KEY_SPEC).build()

    fun generateToken(@NonNull username: String?): String {
        LOG.debug(String.format("Generate token for user [%s]", username))
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

    fun validateToken(@NonNull token: String?): Boolean {
        try {
            jwtParser.parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            LOG.warn("Invalid token")
        }
        return false
    }

    fun getUsernameFromToken(@NonNull token: String?): String {
        return jwtParser.parseClaimsJws(token).body.subject
    }
}
