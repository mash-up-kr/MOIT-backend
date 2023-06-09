package com.mashup.moit.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.security.authentication.JwtAuthentication
import com.mashup.moit.security.authentication.MoitUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
        log.debug("JWT Token Filter")
        try {
            setAuthenticationFromToken(request)
            filterChain.doFilter(request, response)
        } catch (authException: AuthenticationException) {
            log.debug("Authentication Failed: Authorization value=${request.getAuthorization()}, Message=${authException.message}")
            SecurityContextHolder.clearContext()
            response.run {
                status = HttpStatus.UNAUTHORIZED.value()
                addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                val failResponse = MoitApiResponse.fail(MoitExceptionType.AUTH_ERROR, authException.message)
                writer.write(objectMapper.writeValueAsString(failResponse))
            }
        }
    }

    private fun setAuthenticationFromToken(request: HttpServletRequest) {
        if (!isNotCheckEndpoint(request)) {
            val authorization = request.getAuthorization()
            log.debug("Parsing token in header: $authorization - Request path: ${request.requestURI}")
            getToken(authorization)?.run {
                jwtTokenSupporter.extractUserFromToken(this).apply {
                    SecurityContextHolder.getContext().authentication = JwtAuthentication(MoitUser(this))
                }
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
