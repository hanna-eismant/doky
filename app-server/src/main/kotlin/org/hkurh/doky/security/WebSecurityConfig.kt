package org.hkurh.doky.security

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
internal class WebSecurityConfig(private val jwtAuthorizationFilter: JwtAuthorizationFilter) {
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
        corsConfiguration.addAllowedMethod(HttpMethod.PUT)
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }
}
