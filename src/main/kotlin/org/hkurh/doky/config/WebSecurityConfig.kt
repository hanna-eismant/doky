/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky.config

import org.hkurh.doky.security.impl.JwtAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * The [WebSecurityConfig] class is a configuration class that defines the security configuration for the web application.
 *
 * It is annotated with `@EnableWebSecurity` to enable web security features, `@Configuration` to indicate that it is a
 * configuration class, and `@EnableMethodSecurity` with `secured
 * Enabled` set to `true` to enable method level security using the `@Secured` annotation.
 *
 * The class provides two `@Bean` methods that configure the security filter chain and the CORS configuration source.
 */
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
internal class WebSecurityConfig(
    private val jwtAuthorizationFilter: JwtAuthorizationFilter
) {

    @Bean
    @Throws(Exception::class)
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .csrf { it.ignoringRequestMatchers("/**") }
            .sessionManagement { configurer: SessionManagementConfigurer<HttpSecurity?> ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .cors(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    protected fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration().applyPermitDefaultValues()
        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS)
        corsConfiguration.addAllowedMethod(HttpMethod.GET)
        corsConfiguration.addAllowedMethod(HttpMethod.POST)
        corsConfiguration.addAllowedMethod(HttpMethod.PUT)
        corsConfiguration.addAllowedMethod(HttpMethod.HEAD)
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }
}
