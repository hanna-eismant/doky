package org.hkurh.doky.filters;

import static org.hkurh.doky.DokyApplication.SECRET_KEY_SPEC;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final String HEADER = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        LOGGER.debug("Jwt Authorization Filter is invoked.");

        try {
            String token = request.getHeader(HEADER);
            if (isTokenPresented(token)) {
                Claims claims = validateToken(token);
                if (claims.get("authorities") != null) {
                    setupSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                LOGGER.warn("Token is not presented. Clear authorization context.");
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private Claims validateToken(final String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY_SPEC).build();
        Claims claims = jwtParser.parseClaimsJwt(token).getBody();
        return claims;
    }

    private boolean isTokenPresented(final String token) {
        return token != null && !token.isEmpty() && !token.isBlank();
    }

    private void setupSpringAuthentication(final Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get("authorities");

        String principal = claims.getSubject();
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
