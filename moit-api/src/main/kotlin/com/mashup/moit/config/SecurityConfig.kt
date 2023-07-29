package com.mashup.moit.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.security.handler.AuthFailureHandler
import com.mashup.moit.security.handler.HttpStatusAccessDeniedHandler
import com.mashup.moit.security.handler.HttpStatusAuthenticationEntryPoint
import com.mashup.moit.security.jwt.JwtTokenFilter
import com.mashup.moit.security.jwt.JwtTokenSupporter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperty::class)
class SecurityConfig(
    private val logoutHandler: LogoutHandler,
    private val jwtTokenSupporter: JwtTokenSupporter,
    private val objectMapper: ObjectMapper,
    private val securityProperty: SecurityProperty,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .oauth2Login {
                it.defaultSuccessUrl("/api/v1/auth/sign-in/success") // TODO: Use successHandler 
                    .failureHandler(AuthFailureHandler())
            }
            .logout { it.logoutRequestMatcher(AntPathRequestMatcher("/logout")).addLogoutHandler(logoutHandler) }
            .authorizeHttpRequests {
                it.anyRequest().authenticated() // TODO: 추후 인가 추가
            }
            .addFilterBefore(JwtTokenFilter(jwtTokenSupporter, objectMapper), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusAuthenticationEntryPoint())
                it.accessDeniedHandler(HttpStatusAccessDeniedHandler())
            }
            .build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(
                // swagger
                "/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
                // error page
                "/error/**",
                // actuator
                "/am-i-alive/**",
                // Auth endpoints
                "/api/v1/auth/sign-up", "/api/v1/auth/sign-in"
            )
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            securityProperty.allowOrigins?.forEach { addAllowedOrigin(it) }
            addAllowedMethod("*")
            addAllowedHeader("*")
            exposedHeaders = listOf(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)
            maxAge = 3600L
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/api/**", configuration)
        }
    }

}

@ConfigurationProperties(prefix = "moit.security")
data class SecurityProperty(val allowOrigins: List<String>?)
