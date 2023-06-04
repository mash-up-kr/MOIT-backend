package com.mashup.moit.security

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders

class JwtTokenSupport {
    private val logger: Logger = LoggerFactory.getLogger(JwtTokenSupport::class.java)

    /**
     * 요청으로부터 Token 추출
     * - Authorization Header value 로 부터 BEARER 이후 값 추출
     *
     * @param request 유저 요청
     * @return JWT token String
     */
    fun extractToken(request: HttpServletRequest): String {
        val authorization = getAuthorization(request)
        logger.debug("Parsing token in header: {} - Request path: {}", authorization, request.requestURI)
        return authorization.split(SPLIT_DELIMITER)[1]
    }

    /**
     * 응답에 Token 주입
     *
     * @param response 유저에게 보낼 응답
     * @param jwtToken 주입할 JWT 토큰
     */
    fun injectToken(response: HttpServletResponse, jwtToken: String) {
        response.addHeader(HttpHeaders.AUTHORIZATION, "$TOKEN_TYPE $jwtToken")
    }
    
    private fun getAuthorization(request: HttpServletRequest) =
        request.getHeader(HttpHeaders.AUTHORIZATION) ?: throw MoitException.of(MoitExceptionType.INVALID_USER_AUTH_TOKEN)

    companion object {
        const val TOKEN_TYPE: String = "BEARER"
        const val SPLIT_DELIMITER: String = " "
    }

}
