package org.hkurh.doky.security;

import static org.hkurh.doky.DokyApplication.SECRET_KEY_SPEC;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
    private static final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY_SPEC).build();

    public static String generateToken(@NonNull final String username) {
        var currentTime = new DateTime(DateTimeZone.getDefault());
        var expireTokenTime = currentTime.plusDays(1);
        return Jwts.builder()
                .setId("dokyToken")
                .setSubject(username)
                .setIssuedAt(currentTime.toDate())
                .setExpiration(expireTokenTime.toDate())
                .signWith(SECRET_KEY_SPEC, SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(final String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (final Exception e) {
            LOGGER.warn("Invalid token");
        }
        return false;
    }

    public static String getUsernameFromToken(final String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }
}
