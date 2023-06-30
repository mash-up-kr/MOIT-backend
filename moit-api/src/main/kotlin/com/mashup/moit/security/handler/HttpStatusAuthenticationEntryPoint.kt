package com.mashup.moit.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint


class HttpStatusAuthenticationEntryPoint() : AuthenticationEntryPoint {
    private val log: Logger = LoggerFactory.getLogger(HttpStatusAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.debug("Not Authenticated: ${authException.message}")
        response.status = HttpStatus.UNAUTHORIZED.value()
    }
}

