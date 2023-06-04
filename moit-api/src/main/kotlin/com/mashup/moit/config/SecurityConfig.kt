package com.mashup.moit.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val logoutHandler: LogoutHandler
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.oauth2Login { it.defaultSuccessUrl(LOGIN_ENDPOINT) }
            .logout { it.logoutRequestMatcher(AntPathRequestMatcher("/logout")).addLogoutHandler(logoutHandler) }
            .build()
    }

    companion object {
        const val LOGIN_ENDPOINT = "/api/v1/sample/login"
    }

}
