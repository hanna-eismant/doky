/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.security

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hkurh.doky.errorhandling.DokyNotFoundException
import org.hkurh.doky.security.JwtProvider.getUsernameFromToken
import org.hkurh.doky.users.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

/**
 * [JwtAuthorizationFilter] is a class that implements the [OncePerRequestFilter] interface to handle JWT authorization.
 *
 * The filter checks the incoming request for a JWT token in the Authorization header. If a token is found, it is validated and the associated user is authenticated.
 *
 * The filter has a list of anonymous endpoints that do not require token authentication. Requests to these endpoints will pass through the filter without further token checks.
 *
 * If a token is not presented or is invalid, the filter clears the authorization context, and the user is not authenticated.
 *
 * If the token is valid, the filter sets the authentication context with the authenticated user's information.
 *
 * Only requests that pass through the filter will be authenticated and have access to the authenticated user's information.
 */
@Component
class JwtAuthorizationFilter(private val userService: UserService) : OncePerRequestFilter() {
    private val authorizationHeader = "Authorization"
    private val anonymousEndpoints = setOf(
        "/api/register",
        "/api/login",
        "/api/password/reset",
        "/api/password/update"
    )

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        LOG.debug { "Jwt Authorization Filter is invoked." }

        if (anonymousEndpoints.contains(request.requestURI)) {
            LOG.debug { "No check token for request ${request.requestURI}" }
            filterChain.doFilter(request, response)
            return
        }

        try {
            processAuthorization(request, filterChain, response)
        } catch (e: Exception) {
            if (e is JwtException || e is DokyNotFoundException) {
                response.status = HttpServletResponse.SC_FORBIDDEN
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            } else {
                throw e
            }
        }
    }

    private fun processAuthorization(
        request: HttpServletRequest,
        filterChain: FilterChain,
        response: HttpServletResponse
    ) {
        val token = getTokenFromRequest(request)
        if (token != null) {
            val usernameFromToken = getUsernameFromToken(token)
            userService.findUserByUid(usernameFromToken)?.let {
                val dokyUserDetails = DokyUserDetails(it)
                val authenticationToken =
                    UsernamePasswordAuthenticationToken(dokyUserDetails, null, dokyUserDetails.getAuthorities())
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        } else {
            LOG.warn { "Token is not presented. Clear authorization context." }
            SecurityContextHolder.clearContext()
        }
        filterChain.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val token: String? = request.getHeader(authorizationHeader)
        return if (!token.isNullOrBlank() && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
