package com.mashup.moit.security.jwt

import com.mashup.moit.config.AuthConfig
import com.mashup.moit.domain.config.JasyptConfig
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.security.authentication.UserInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor

@Disabled("토큰 생성용 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(JasyptConfig::class, AuthConfig::class, JacksonAutoConfiguration::class, UserService::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JwtTokenCreateTest(
    private val jwtTokenSupporter: JwtTokenSupporter,
    private val userService: UserService
) {

    @Test
    fun createTokenTest() {
        // given
        val user = userService.findUserById(4L)
        val userInfo = UserInfo.from(user = user)

        // when
        val token = jwtTokenSupporter.createToken(userInfo)

        // then
        println("token = Bearer $token")
        assertThat(token.length).isPositive()
    }

}
