package com.intezya.unihub.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authFilter: AuthFilter,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .cors { cors ->
            cors.configurationSource { request ->
                val config = CorsConfiguration()
                config.allowedOrigins = listOf("*")
                config.allowedMethods = listOf("*")
                config.allowedHeaders = listOf("*")
                config.exposedHeaders = listOf("*")
                config.allowCredentials = false
                config.maxAge = 3600L
                config
            }
        }
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
        .authorizeHttpRequests { auth ->
            auth
                .requestMatchers(
                    "/auth/max",
                    "/api/user/change-role",
                    "/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                ).permitAll()
                .anyRequest().authenticated()
        }
        .build()
}
