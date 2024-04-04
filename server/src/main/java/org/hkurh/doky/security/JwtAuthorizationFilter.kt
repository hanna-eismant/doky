package org.hkurh.doky.security

import io.jsonwebtoken.ClaimJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hkurh.doky.errorhandling.DokyNotFoundException
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
    private val authorizationHeader = "Authorization"
    private val anonymousEndpoints = arrayOf("/register", "/login")

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        LOG.debug("Jwt Authorization Filter is invoked.")

        if (anonymousEndpoints.contains(request.requestURI)) {
            LOG.debug("No check token for request ${request.requestURI}")
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token = getTokenFromRequest(request)
            if (token != null) {
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
        } catch (e: Exception) {
            when (e) {
                is JwtException,
                is DokyNotFoundException -> {
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
                }

                else -> throw e
            }
        }
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val token: String? = request.getHeader(authorizationHeader)
        return if (!token.isNullOrBlank() && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JwtAuthorizationFilter::class.java)
    }
}
