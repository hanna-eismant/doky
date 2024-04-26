package org.hkurh.doky.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hkurh.doky.DokyApplication
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.stereotype.Component

/**
 * [JwtProvider] is a utility class responsible for generating and parsing JSON Web Tokens (JWTs).
 */
@Component
object JwtProvider {
    private val jwtParser = Jwts.parserBuilder().setSigningKey(DokyApplication.SECRET_KEY_SPEC).build()

    /**
     * Generates a token for the given username.
     *
     * @param username The username for which to generate the token.
     * @return The generated token.
     */
    fun generateToken(username: String): String {
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

    /**
     * Retrieves the username from the provided token.
     *
     * @param token The token from which to extract the username.
     * @return The extracted username.
     */
    fun getUsernameFromToken(token: String): String {
        return jwtParser.parseClaimsJws(token).body.subject
    }
}
