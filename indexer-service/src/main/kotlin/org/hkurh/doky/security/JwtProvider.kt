package org.hkurh.doky.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hkurh.doky.IndexerServiceApplication
import org.hkurh.doky.users.db.UserEntity
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.stereotype.Component
import java.util.*


/**
 * [JwtProvider] is a utility class responsible for generating and parsing JSON Web Tokens (JWTs).
 */
@Component
object JwtProvider {
    private val jwtParser = Jwts.parserBuilder().setSigningKey(IndexerServiceApplication.SECRET_KEY_SPEC).build()
    private const val ID_KEY = "token_id"
    private const val USERNAME_KEY = "username"
    private const val ROLES_KEY = "roles"
    private const val AUTH_TOKEN_ID = "dokyAuthToken"
    private const val DOWNLOAD_TOKEN_ID = "dokyDownloadToken"

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
        val claims = mapOf(
            ID_KEY to AUTH_TOKEN_ID,
            USERNAME_KEY to username,
            ROLES_KEY to roles
        )
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentTime.toDate())
            .setExpiration(expireTokenTime.toDate())
            .signWith(IndexerServiceApplication.SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Generates a download token for a given user. The token is a JWT that includes claims related to the user and has a 10-minute expiration time.
     *
     * @param user The [UserEntity] object representing the user for whom the download token is generated.
     * @return A JWT string representing the generated download token.
     */
    fun generateDownloadToken(user: UserEntity): String {
        val currentTime = DateTime(DateTimeZone.getDefault())
        val expireTokenTime = currentTime.plusMinutes(10)
        val claims = mapOf(
            ID_KEY to DOWNLOAD_TOKEN_ID
        )
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentTime.toDate())
            .setExpiration(expireTokenTime.toDate())
            .signWith(IndexerServiceApplication.SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
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

    /**
     * Validates if the given download token is valid.
     * This method parses the token, checks its ID, and ensures it is not expired.
     *
     * @param token The JWT download token to be validated.
     * @return True if the token is valid and not expired; otherwise, false.
     */
    fun isDownloadTokenValid(token: String): Boolean {
        val claims = jwtParser.parseClaimsJws(token).body
        if (claims[ID_KEY] != DOWNLOAD_TOKEN_ID) return false
        val expiration = claims.expiration
        return expiration.after(Date())
    }
}
