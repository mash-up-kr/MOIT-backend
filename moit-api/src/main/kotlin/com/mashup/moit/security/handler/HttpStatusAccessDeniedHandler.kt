package com.mashup.moit.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

class HttpStatusAccessDeniedHandler : AccessDeniedHandler {
    private val log: Logger = LoggerFactory.getLogger(HttpStatusAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.debug("Access Denied: ${accessDeniedException.message}")
        response.status = HttpStatus.FORBIDDEN.value()
    }
}
