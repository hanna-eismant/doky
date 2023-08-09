package org.hkurh.doky.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.hkurh.doky.DokyApplication.SECRET_KEY_SPEC;

@Component
public class JwtProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtProvider.class);
    private static final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY_SPEC).build();

    public static String generateToken(@NonNull String username) {
        LOG.debug(format("Generate token for user [%s]", username));
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

    public static boolean validateToken(@NonNull String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (final Exception e) {
            LOG.warn("Invalid token");
        }
        return false;
    }

    public static String getUsernameFromToken(@NonNull String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }
}
