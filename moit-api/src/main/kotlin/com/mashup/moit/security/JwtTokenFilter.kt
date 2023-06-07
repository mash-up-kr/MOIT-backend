package com.mashup.moit.security

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.config.SecurityConfig
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtTokenFilter(
    private val jwtTokenSupporter: JwtTokenSupporter
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(JwtTokenFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (!request.requestURI.startsWith(SecurityConfig.LOGIN_ENDPOINT)) {
            extractUserAndSetAuth(request)
        }
        filterChain.doFilter(request, response)
    }

    private fun extractUserAndSetAuth(request: HttpServletRequest) {
        val authorization = request.getAuthorization()
        log.debug("Parsing token in header: $authorization - Request path: ${request.requestURI}")
        authorization.split(AUTH_PROVIDER_SPLIT_DELIMITER)
            .takeIf { it[0] == JwtTokenSupporter.BEARER_TOKEN_PREFIX }?.get(1)
            ?.run {
                val user = jwtTokenSupporter.extractUserFromToken(this)
                SecurityContextHolder.getContext().authentication = JwtAuthentication(user)
            }
    }

    private fun HttpServletRequest.getAuthorization() =
        this.getHeader(HttpHeaders.AUTHORIZATION) ?: throw MoitException.of(MoitExceptionType.INVALID_USER_AUTH_TOKEN)

    companion object {
        const val AUTH_PROVIDER_SPLIT_DELIMITER: String = " "
    }

}
