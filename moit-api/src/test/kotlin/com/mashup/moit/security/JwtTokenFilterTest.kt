package com.mashup.moit.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.jwt.JwtTokenFilter
import com.mashup.moit.security.jwt.JwtTokenSupporter
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class JwtTokenFilterTest : DescribeSpec() {

    private val jwtTokenFilter: JwtTokenFilter
    private val jwtTokenSupporter = mockk<JwtTokenSupporter>()
    private val objectMapper = mockk<ObjectMapper>()
    private val filterChain = mockk<FilterChain>()

    init {
        this.jwtTokenFilter = JwtTokenFilter(jwtTokenSupporter, objectMapper)

        beforeContainer {
            every { filterChain.doFilter(any(), any()) } just Runs
        }

        afterContainer {
            clearAllMocks()
        }

        describe("JwtTokenFilter로") {
            context("token 검사가 필요없는 엔드포인트에서 요청이 온 경우") {
                val request = MockHttpServletRequest("GET", "/error")
                val response = MockHttpServletResponse()
                jwtTokenFilter.doFilter(request, response, filterChain)

                it("토큰으로부터 유저를 추출하지 않는다") {
                    verify(exactly = 0) { jwtTokenSupporter.createToken(any()) }
                }

                it("SecurityHolder 에 인증정보를 넣지 않는다") {
                    SecurityContextHolder.getContext().authentication shouldBe null
                }
            }

            context("token 검사가 필요한 엔드포인트에서 요청이 온 경우") {
                // set test data
                val testToken = "test-jwt-token"
                val testEmail = "testEmail"
                val testNickName = "testNickname"
                val userInfo = UserInfo(
                    id = 1L,
                    providerUniqueKey = "",
                    email = testEmail,
                    profileImage = 1,
                    nickname = testNickName,
                    roles = emptySet()
                )
                
                every { jwtTokenSupporter.extractUserFromToken(testToken) } returns userInfo

                val request = MockHttpServletRequest("GET", "/must/login-user/test").apply {
                    addHeader(HttpHeaders.AUTHORIZATION, "${JwtTokenSupporter.BEARER_TOKEN_PREFIX} $testToken")
                }
                val response = MockHttpServletResponse()
                jwtTokenFilter.doFilter(request, response, filterChain)

                it("Authorization header로 부터 token을 추출해 SecurityContextHolder에 저장한다") {
                    val authentication = SecurityContextHolder.getContext().authentication
                    authentication.name shouldBe testNickName
                    authentication.principal shouldBe userInfo
                }
            }
        }
    }
}
