package com.mashup.moit.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.sample.SampleUser
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Base64
import java.util.Date

class JwtTokenSupporter(
    key: String,
    private val mapper: ObjectMapper
) {
    private val secretKey: Key
    private val jwtParser: JwtParser

    init {
        val encodedKey = Base64.getEncoder().encodeToString(key.toByteArray())
        this.secretKey = Keys.hmacShaKeyFor(encodedKey.toByteArray())
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build()
    }

    /**
     * Moit JWT Token 생성
     *
     * @param userInfo 로그인 유저
     * @return JWT Token
     */
    fun createToken(userInfo: UserInfo): String {
        val issuerDateTime = LocalDateTime.now(ASIA_SEOUL_ZONE)
        val expiredDateTime = issuerDateTime.plusDays(DAY_30)
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject("jwt-user-${userInfo.nickname}")
            .setAudience("${userInfo.providerUniqueKey}|${userInfo.id}|${userInfo.nickname}")
            .setIssuer("https://github.com/mash-up-kr/MOIT-backend")
            .setIssuedAt(issuerDateTime.convertToDate())
            .setExpiration(expiredDateTime.convertToDate())
            .claim(CLAIM_INFO_KEY, userInfo)
            .signWith(secretKey)
            .compact()
    }

    /**
     * Moit JWT Token 으로부터 User 추출
     *
     * @param token Moit Jwt Token
     * @return MoitUser
     */
    fun extractUserFromToken(token: String): SampleUser {
        return jwtParser.parseClaimsJws(token).body?.let {
            mapper.convertValue(it[CLAIM_INFO_KEY], SampleUser::class.java)
        } ?: throw MoitException.of(MoitExceptionType.INVALID_USER_AUTH_TOKEN)
    }

    /**
     * jjwt 지원을 위해 LocalDateTime to Date 변환하는 확장함수
     *
     * @return date
     */
    private fun LocalDateTime.convertToDate(): Date = Date.from(this.toInstant(ASIA_SEOUL_OFFSET))

    companion object {
        const val BEARER_TOKEN_PREFIX = "BEARER"
        const val CLAIM_INFO_KEY = "info"
        private const val DAY_30 = 30L
        private val ASIA_SEOUL_ZONE = ZoneId.of("Asia/Seoul")
        private val ASIA_SEOUL_OFFSET = ZoneOffset.of("+09:00")
    }

}

