package com.mashup.moit.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.security.handler.AuthFailureHandler
import com.mashup.moit.security.handler.HttpStatusAccessDeniedHandler
import com.mashup.moit.security.handler.HttpStatusAuthenticationEntryPoint
import com.mashup.moit.security.jwt.JwtTokenFilter
import com.mashup.moit.security.jwt.JwtTokenSupporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val logoutHandler: LogoutHandler,
    private val jwtTokenSupporter: JwtTokenSupporter,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .oauth2Login {
                it.loginPage("/api/v1/auth/login")
                    .defaultSuccessUrl("/api/v1/auth/login/success") // TODO: Use successHandler 
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
                "/**", // TODO: 개발 중이므로 다 제외
                "/error/**",
                "/am-i-alive/**",
                "/api/v1/auth/register"
            )
        }
    }

}
