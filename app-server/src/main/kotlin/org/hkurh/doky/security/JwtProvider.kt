package org.hkurh.doky.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hkurh.doky.DokyApplication
import org.hkurh.doky.users.db.UserEntity
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.stereotype.Component

/**
 * [JwtProvider] is a utility class responsible for generating and parsing JSON Web Tokens (JWTs).
 */
@Component
object JwtProvider {
    private val jwtParser = Jwts.parserBuilder().setSigningKey(DokyApplication.SECRET_KEY_SPEC).build()
    private const val USERNAME_KEY = "username"
    private const val ROLES_KEY = "roles"

    /**
     * Generates a token for the given username.
     *
     * @param username The username for which to generate the token.
     * @param roles The list of roles associated to user.
     * @return The generated token.
     */
    fun generateToken(username: String, roles: Set<Any>): String {
        val currentTime = DateTime(DateTimeZone.getDefault())
        val expireTokenTime = currentTime.plusDays(1)
        val claims = mapOf(USERNAME_KEY to username, ROLES_KEY to roles)
        return Jwts.builder()
            .setId("dokyToken")
            .setClaims(claims)
            .setIssuedAt(currentTime.toDate())
            .setExpiration(expireTokenTime.toDate())
            .signWith(DokyApplication.SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Generates a download token for a given user and document. The token is a JWT
     * with an expiration time of 10 minutes and includes claims containing the user ID
     * and document ID.
     *
     * @param user the user for whom the token is being generated
     * @return a JWT download token as a string
     */
    fun generateDownloadToken(user: UserEntity): String {
        val currentTime = DateTime(DateTimeZone.getDefault())
        val expireTokenTime = currentTime.plusMinutes(10)
        val claims = mapOf("user" to user.id)
        return Jwts.builder()
            .setId("dokyDownloadToken")
            .setClaims(claims)
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
        return jwtParser.parseClaimsJws(token).body[USERNAME_KEY].toString()
    }
}
