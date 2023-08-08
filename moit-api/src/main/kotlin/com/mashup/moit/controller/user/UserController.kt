package com.mashup.moit.controller.user

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.controller.user.dto.UserFcmTokenRequest
import com.mashup.moit.facade.UserFacade
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User API", description = "User 관련 API 입니다.")
@RequestMapping("/api/v1/user")
@RestController
class UserController(
    private val userFacade: UserFacade,
) {

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회하는 API")
    @GetMapping
    fun getUserInformation(@GetAuth userInfo: UserInfo): MoitApiResponse<UserInfo> {
        return MoitApiResponse.success(userInfo)
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 API")
    @DeleteMapping
    fun withdraw(@GetAuth userInfo: UserInfo): MoitApiResponse<Unit> {
        userFacade.deleteById(userInfo.id)
        return MoitApiResponse.success()
    }

    @Operation(summary = "FCM Token 갱신", description = "Update FCM Token API")
    @PostMapping("/fcm-token")
    fun updateFcmToken(@GetAuth userInfo: UserInfo, @RequestBody request: UserFcmTokenRequest): MoitApiResponse<Unit> {
        userFacade.updateFcmToken(userInfo.id, request.fcmToken)
        return MoitApiResponse.success()
    }

}
