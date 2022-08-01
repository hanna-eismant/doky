package org.hkurh.doky.filters;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.entities.UserEntity;
import org.hkurh.doky.security.JwtProvider;
import org.hkurh.doky.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hkurh.doky.DokyApplication.SECRET_KEY_SPEC;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtProvider jwtProvider;
    private UserService userService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        LOGGER.debug("Jwt Authorization Filter is invoked.");

        try {
            final String token = getTokenFromRequest(request);
            if (token != null && JwtProvider.validateToken(token)) {
                final String usernameFromToken = JwtProvider.getUsernameFromToken(token);
                final UserEntity currentUser = getUserService().findUserByUid(usernameFromToken);
                final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY_SPEC).build();
                final Claims claims = jwtParser.parseClaimsJwt(token).getBody();
                setupSpringAuthentication(claims);
            } else {
                LOGGER.warn("Token is not presented. Clear authorization context.");
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (final ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private static void setupSpringAuthentication(final Claims claims) {
        final List<String> authorities = (List<String>) claims.get("authorities");

        final String principal = claims.getSubject();
        final List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static String getTokenFromRequest(final HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    public JwtProvider getJwtProvider() {
        return jwtProvider;
    }

    @Autowired
    public void setJwtProvider(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    private UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
