package com.mashup.moit.controller

import com.mashup.moit.facade.UserFacade
import com.mashup.moit.security.JwtTokenSupporter
import com.mashup.moit.security.OidcUser.toBeforeSignUpInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.*
import java.net.URI

@Tag(name = "SignUp/SignIn API", description = "회원가입 및 로그인 Api 입니다.")
@RequestMapping
@RestController
class AuthController(
    private val userFacade: UserFacade,
    private val jwtTokenSupporter: JwtTokenSupporter,
) {

    @Operation(summary = "로그인 시도", description = "로그인 시도 API - 웹 뷰로 지원 (Auth0 이용)")
    @GetMapping(SIGN_IN_ENDPOINT)
    fun signIn(): ResponseEntity<Unit> {
        val headers = HttpHeaders()
        headers.location = URI.create("/oauth2/authorization/auth0")
        return ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY)
    }

    @Operation(summary = "로그인 성공 (리다이렉트용)", description = "로그인 성공 API - 서버 단 Redirect 용")
    @GetMapping(SIGN_IN_SUCCESS_ENDPOINT)
    fun signInSuccess(@AuthenticationPrincipal oidcUser: OidcUser): ResponseEntity<Any> {
        val user = userFacade.findByProviderUniqueKey(oidcUser)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(oidcUser.toBeforeSignUpInfo())

        val jwtToken = jwtTokenSupporter.createToken(oidcUser, user)
        return ResponseEntity.ok() // TODO: client와 야이기하고 no_content로 맞출지 이야기하보기
            .header(HttpHeaders.AUTHORIZATION, "${JwtTokenSupporter.BEARER_TOKEN_PREFIX} $jwtToken")
            .build()
    }

    companion object {
        const val SIGN_IN_ENDPOINT = "/api/v1/auth/login"
        const val SIGN_IN_SUCCESS_ENDPOINT = "/api/v1/auth/login/success"
        const val SIGN_UP_SUCCESS_ENDPOINT = "/api/v1/auth/register"
    }

}
