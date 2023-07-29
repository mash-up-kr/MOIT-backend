package com.mashup.moit.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.domain.user.UserRole
import com.mashup.moit.security.authentication.UserInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JwtTokenSupporterTest {

    @Test
    fun createTokenTest() {
        // given
        val testKey = "abcd1234!@#$-=-=최고"
        val objectMapper = ObjectMapper()
        val jwtTokenSupporter = JwtTokenSupporter(testKey, objectMapper)
        val userInfo = UserInfo(
            1L,
            "auth0|abc@naver.com",
            "나는야 최고고",
            1,
            "abc@abc.com",
            setOf(UserRole.USER)
        )

        // when
        val token = jwtTokenSupporter.createToken(userInfo)

        // then
        println("token = Bearer $token")
        assertThat(token.length).isPositive()
    }

}
