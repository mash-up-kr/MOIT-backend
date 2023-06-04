package com.mashup.moit.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.sample.SampleUser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Base64
import java.util.Date

class JwtTokenSupporter(key: String, mapper: ObjectMapper) {
    private val secretKey: Key
    private val mapper: ObjectMapper

    init {
        val encodedKey = Base64.getEncoder().encodeToString(key.toByteArray())
        this.secretKey = Keys.hmacShaKeyFor(encodedKey.toByteArray())
        this.mapper = mapper
    }

    /**
     * Moit JWT Token 생성
     *
     * @param user 로그인 유저
     * @return JWT Token
     */
    fun createToken(user: OidcUser): String {
        val issuerDateTime = LocalDateTime.now(ASIA_SEOUL_ZONE)
        val expiredDateTime = issuerDateTime.plusDays(DAY_30)
        val subject = user.getAttribute<String>(CLAIM_SUB_KEY)
        val audience = user.getAttribute<List<String>>(CLAIM_AUD_KEY)?.get(0)
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(subject)
            .setAudience(audience)
            .setIssuer("https://github.com/mash-up-kr/MOIT-backend")
            .setIssuedAt(issuerDateTime.convertToDate())
            .setExpiration(expiredDateTime.convertToDate())
            .claim(CLAIM_INFO_KEY, convertToUser(user))
            .signWith(secretKey)
            .compact()
    }

    /**
     * Auth0 로그인 유저를 Moit 유저 클래스로 변환
     * TODO: SampleUser -> Entity Data User 전환 필요
     *
     * @param user Auth0 User
     * @return MoitUser
     */
    private fun convertToUser(user: OidcUser): SampleUser {
        return user.userInfo!!.let {
            val email = it.getClaimAsString("email")
            val authProvider = getAuthProvider(it.getClaimAsString(CLAIM_SUB_KEY))
            val providerUniqueKey = "$authProvider$PROVIDER_SPLIT_DELIMITER$email"
            SampleUser(
                providerUniqueKey = providerUniqueKey,
                email = email,
                profileImage = it.getClaimAsString("picture"),
                nickname = it.getClaimAsString("nickname")
            )
        }
    }

    /**
     * Auth0 Claim 정보로부터 OAuth Provider 추출
     *
     * @param jwtClaim Auth0 JWT Claim
     * @return OAuth provider
     */
    private fun getAuthProvider(jwtClaim: String): String {
        val lastIndex = jwtClaim.lastIndexOf(PROVIDER_SPLIT_DELIMITER)
        return jwtClaim.takeIf { lastIndex > 0 }?.substring(0, lastIndex)
            ?: throw MoitException.of(MoitExceptionType.INVALID_AUTH_PROVIDER)
    }

    /**
     * jjwt 지원을 위해 LocalDateTime to Date 변환하는 확장함수
     *
     * @param dateTime LocalDateTime
     * @return date
     */
    private fun LocalDateTime.convertToDate(): Date = Date.from(this.toInstant(ZoneOffset.of("+09:00")))

    companion object {
        private const val PROVIDER_SPLIT_DELIMITER = "|"
        private const val DAY_30 = 30L
        private const val CLAIM_INFO_KEY = "info"
        private const val CLAIM_AUD_KEY = "aud"
        private const val CLAIM_SUB_KEY = "sub"
        private val ASIA_SEOUL_ZONE = ZoneId.of("Asia/Seoul")
    }

}

