package com.mashup.moit.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler


class AuthFailureHandler : AuthenticationFailureHandler {
    private val log: Logger = LoggerFactory.getLogger(AuthFailureHandler::class.java)

    override fun onAuthenticationFailure(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        authenticationException: AuthenticationException
    ) {
        log.debug("Fail Authentication: ${authenticationException.message}")
        httpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
    }
}

