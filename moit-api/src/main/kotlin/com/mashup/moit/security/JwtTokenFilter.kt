package com.mashup.moit.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.common.exception.MoitExceptionType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtTokenFilter(
    private val jwtTokenSupporter: JwtTokenSupporter,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(JwtTokenFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        log.info("JWT Token Filter")
        try {
            setAuthenticationFromToken(request)
            filterChain.doFilter(request, response)
        } catch (authException: AuthenticationException) {
            SecurityContextHolder.clearContext()
            response.run {
                val failResponse = MoitApiResponse.fail(MoitExceptionType.AUTH_ERROR, authException.message)
                status = HttpStatus.UNAUTHORIZED.value()
                writer.write(objectMapper.writeValueAsString(failResponse))
            }
        }
    }

    private fun setAuthenticationFromToken(request: HttpServletRequest) {
        if (!isNotCheckEndpoint(request)) {
            val authorization = request.getAuthorization()
            log.debug("Parsing token in header: $authorization - Request path: ${request.requestURI}")
            getToken(authorization)?.run {
                val user = jwtTokenSupporter.extractUserFromToken(this)
                SecurityContextHolder.getContext().authentication = JwtAuthentication(user)
            } ?: throw BadCredentialsException(MoitExceptionType.INVALID_USER_AUTH_TOKEN.message)
        }
    }

    private fun isNotCheckEndpoint(request: HttpServletRequest) = NOT_CHECK_ENDPOINTS.stream().anyMatch { request.requestURI.startsWith(it) }

    private fun HttpServletRequest.getAuthorization() =
        this.getHeader(HttpHeaders.AUTHORIZATION) ?: throw BadCredentialsException(MoitExceptionType.EMPTY_AUTHORIZATION_HEADER.message)

    private fun getToken(authorization: String) = authorization.split(AUTH_PROVIDER_SPLIT_DELIMITER)
        .takeIf { it[0] == JwtTokenSupporter.BEARER_TOKEN_PREFIX }
        ?.get(1)

    companion object {
        private const val AUTH_PROVIDER_SPLIT_DELIMITER: String = " "
        private val NOT_CHECK_ENDPOINTS = listOf(
            // Common
            "/favicon.ico",
            "/error",
            // Swagger
            "/api-docs", "/swagger-ui", "/swagger-resources", "/v3/api-docs",
            // SignIn/SignUp Endpoints
            "/api/v1/auth",
            "/oauth2",
            "/login/oauth2",
        )
    }

}
