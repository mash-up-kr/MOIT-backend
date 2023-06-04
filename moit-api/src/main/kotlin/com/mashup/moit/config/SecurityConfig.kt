package com.mashup.moit.config

import com.mashup.moit.security.JwtTokenFilter
import com.mashup.moit.security.JwtTokenSupporter
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
    private val jwtTokenSupporter: JwtTokenSupporter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .oauth2Login { it.defaultSuccessUrl(LOGIN_ENDPOINT) }
            .logout { it.logoutRequestMatcher(AntPathRequestMatcher("/logout")).addLogoutHandler(logoutHandler) }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .addFilterBefore(JwtTokenFilter(jwtTokenSupporter), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(
                "/**", // TODO: update to authenticated() when complete security settings
                "/am-i-alive/**",
            )
        }
    }

    companion object {
        const val LOGIN_ENDPOINT = "/api/v1/sample/login"
    }

}
