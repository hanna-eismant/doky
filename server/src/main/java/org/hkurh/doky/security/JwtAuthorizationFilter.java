package org.hkurh.doky.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.hkurh.doky.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        LOG.debug("Jwt Authorization Filter is invoked.");
        try {
            var token = getTokenFromRequest(request);
            if (token != null && JwtProvider.validateToken(token)) {
                var usernameFromToken = JwtProvider.getUsernameFromToken(token);
                var currentUser = getUserService().findUserByUid(usernameFromToken);
                var dokyUserDetails = DokyUserDetails.createUserDetails(currentUser);
                var authenticationToken = new UsernamePasswordAuthenticationToken(dokyUserDetails, null, dokyUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                LOG.warn("Token is not presented. Clear authorization context.");
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (final ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private static String getTokenFromRequest(final HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    private UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
