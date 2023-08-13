package org.hkurh.doky.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.security.JwtProvider.getUsernameFromToken
import org.hkurh.doky.security.JwtProvider.validateToken
import org.hkurh.doky.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthorizationFilter(private val userService: UserService) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        LOG.debug("Jwt Authorization Filter is invoked.")
        try {
            val token = getTokenFromRequest(request)
            if (token != null && validateToken(token)) {
                val usernameFromToken = getUsernameFromToken(token)
                val currentUser = userService.findUserByUid(usernameFromToken)
                val dokyUserDetails: DokyUserDetails = DokyUserDetails.createUserDetails(currentUser!!)
                val authenticationToken = UsernamePasswordAuthenticationToken(dokyUserDetails, null, dokyUserDetails.getAuthorities())
                SecurityContextHolder.getContext().authentication = authenticationToken
            } else {
                LOG.warn("Token is not presented. Clear authorization context.")
                SecurityContextHolder.clearContext()
            }
            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
        } catch (e: UnsupportedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
        } catch (e: MalformedJwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JwtAuthorizationFilter::class.java)
        private const val AUTHORIZATION_HEADER = "Authorization"
        private fun getTokenFromRequest(request: HttpServletRequest): String? {
            val token = request.getHeader(AUTHORIZATION_HEADER)
            return if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
                token.substring(7)
            } else null
        }
    }
}
