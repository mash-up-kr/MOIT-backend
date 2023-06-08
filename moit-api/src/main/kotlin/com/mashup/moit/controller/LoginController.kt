package com.mashup.moit.controller

import com.mashup.moit.security.JwtTokenSupporter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.*
import java.net.URI

@Tag(name = "Sample", description = "Sample Login Api 입니다.")
@RequestMapping
@RestController
class LoginController(
    private val jwtTokenSupporter: JwtTokenSupporter
) {

    @Operation(summary = "login API", description = "Login API")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "OK")])
    @GetMapping(LOGIN_ENDPOINT)
    fun login(): ResponseEntity<Unit> {
        val headers = HttpHeaders()
        headers.location = URI.create("/oauth2/authorization/auth0")
        return ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY)
    }

    @Operation(summary = "login success page", description = "Login Success API")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "OK")])
    @GetMapping(LOGIN_SUCCESS_ENDPOINT)
    fun loginSuccess(@AuthenticationPrincipal user: OidcUser): ResponseEntity<Unit> {
        val jwtToken = jwtTokenSupporter.createToken(user)
        // TODO: client와 야이기하고 no_content로 맞출지 이야기하보기
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "${JwtTokenSupporter.BEARER_TOKEN_PREFIX} $jwtToken")
            .build()
    }

    companion object {
        const val LOGIN_ENDPOINT = "/api/v1/login"
        const val LOGIN_SUCCESS_ENDPOINT = "/api/v1/login/success"
    }

}
