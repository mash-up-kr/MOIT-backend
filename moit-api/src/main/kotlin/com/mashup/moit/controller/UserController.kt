package com.mashup.moit.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User API", description = "User 관련 API 입니다.")
@RequestMapping("/api/v1/user")
@RestController
class UserController {

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회하는 API")
    @GetMapping
    fun getUserInformation(@GetAuth userInfo: UserInfo): MoitApiResponse<UserInfo> {
        return MoitApiResponse.success(userInfo)
    }

}
