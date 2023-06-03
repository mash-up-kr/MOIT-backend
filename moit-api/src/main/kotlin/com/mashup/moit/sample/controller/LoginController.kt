package com.mashup.moit.sample.controller

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
@RequestMapping("/api/v1/sample/login")
@RestController
class LoginController {

    @Operation(summary = "login API", description = "Login API")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "OK")])
    @GetMapping
    fun login(@AuthenticationPrincipal principal: OidcUser?): ResponseEntity<String> {
        if (principal != null) {
            return ResponseEntity.ok(principal.toString())
        }
        val headers = HttpHeaders()
        headers.location = URI.create("/oauth2/authorization/auth0")
        return ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY)
    }

}
