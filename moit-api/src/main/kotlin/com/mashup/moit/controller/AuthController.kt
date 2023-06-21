package com.mashup.moit.controller

import com.mashup.moit.controller.dto.UserRegisterRequest
import com.mashup.moit.facade.UserFacade
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.authentication.toBeforeSignUpInfo
import com.mashup.moit.security.jwt.JwtTokenSupporter
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
@RequestMapping("/api/v1/auth")
@RestController
class AuthController(
    private val userFacade: UserFacade,
    private val jwtTokenSupporter: JwtTokenSupporter,
) {

    @Operation(summary = "로그인 시도", description = "로그인 시도 API - 웹 뷰로 지원 (Auth0 이용)")
    @GetMapping("/sign-in")
    fun signIn(): ResponseEntity<Unit> {
        val headers = HttpHeaders()
        headers.location = URI.create("/oauth2/authorization/auth0")
        return ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY)
    }

    @Operation(summary = "로그인 성공 (리다이렉트용)", description = "로그인 성공 API - 서버 단 Redirect 용")
    @GetMapping("/sign-in/success")
    fun signInSuccess(@AuthenticationPrincipal oidcUser: OidcUser): ResponseEntity<Any> {
        val user = userFacade.findByProviderUniqueKey(oidcUser)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(oidcUser.toBeforeSignUpInfo())

        val jwtToken = jwtTokenSupporter.createToken(UserInfo.from(user))
        return ResponseEntity.noContent()
            .header(HttpHeaders.AUTHORIZATION, "${JwtTokenSupporter.BEARER_TOKEN_PREFIX} $jwtToken")
            .build()
    }

    @Operation(summary = "회원가입", description = "회원가입 API")
    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: UserRegisterRequest): ResponseEntity<Any> {
        val jwtToken = userFacade.createUser(request).let { jwtTokenSupporter.createToken(UserInfo.from(it)) }
        return ResponseEntity.noContent()
            .header(HttpHeaders.AUTHORIZATION, "${JwtTokenSupporter.BEARER_TOKEN_PREFIX} $jwtToken")
            .build()
    }

}
