package com.mashup.moit.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Base64
import java.util.Date

class JwtTokenSupporterTest : BehaviorSpec() {
    private val jwtTokenSupporter: JwtTokenSupporter
    private val jwtParser: JwtParser
    private val objectMapper: ObjectMapper

    init {
        val objectMapper = ObjectMapper()
        val secretKey = "secret-key-for-test-!@#$"
        val encodedKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())

        this.jwtTokenSupporter = JwtTokenSupporter(secretKey, objectMapper)
        this.jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(encodedKey.toByteArray())).build()
        this.objectMapper = objectMapper

        Given("User 정보가 주어진 상황에서") {
            // define user properties
            val providerInfo = "auth|provider-info"
            val tokenSubject = "$providerInfo|unique-key"
            val tokenAudience = "audience-value"
            val claims = mapOf(
                "sub" to tokenSubject,
                "aud" to listOf(tokenAudience),
                "email" to "abc@moit.com",
                "picture" to "profile-image",
                "nickname" to "user-nickname"
            )

            // define test user
            val userInfo = OidcUserInfo(claims)
            val idToken = OidcIdToken("token-value-auth", NOW.toInstant(), NOW.plusDays(1L).toInstant(), claims)
            val user: OidcUser = DefaultOidcUser(null, idToken, userInfo)

            When("JWT 토큰 생성했을 때") {
                val jwtToken = jwtTokenSupporter.createToken(user)
                val jwt = jwtParser.parseClaimsJws(jwtToken)

                Then("토큰의 type이 JWT여야 한다") {
                    jwt.header.type shouldBe "JWT"
                }

                Then("토큰의 subject, audience 가 전달된 유저정보에서 추출되어 적용되었어야 한다") {
                    jwt.body.subject shouldBe tokenSubject
                    jwt.body.audience shouldBe tokenAudience
                }

                Then("토큰의 IssuedAt/Expiration 는 현재보다 이후여야 한다") {
                    jwt.body.issuedAt.isAfter(NOW.toLocalDate())
                    jwt.body.expiration.isAfter(NOW.toLocalDate())
                }

                Then("토큰의 Expiration 는 IssuedAt 보다 30일 이후여야 한다") {
                    val millisOfDay30 = Duration.ofDays(30).toMillis()
                    val issuedAt = jwt.body.issuedAt.toInstant().toEpochMilli()
                    val expiredAt = jwt.body.expiration.toInstant().toEpochMilli()
                    expiredAt - issuedAt shouldBe millisOfDay30
                }

                Then("토큰의 user 정보가 제대로 설정되어야 한다") {
                    // TODO: 이후 유저 클래스 정의될 때 테스트 보완
                    // val user = objectMapper.readValue(jwt.body["info"] as String, SampleUser::class.java)
                }
            }
        }
    }

    private fun LocalDateTime.toInstant(): Instant {
        return this.toInstant(ZoneOffset.UTC)
    }

    private fun Date.isAfter(localDate: LocalDate): Boolean {
        return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .isAfter(localDate)
    }

    companion object {
        private val NOW = LocalDateTime.now()
    }
    
}
