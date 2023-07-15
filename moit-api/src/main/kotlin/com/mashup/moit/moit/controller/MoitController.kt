package com.mashup.moit.moit.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.moit.controller.dto.MoitCreateRequest
import com.mashup.moit.moit.controller.dto.MoitCreateResponse
import com.mashup.moit.moit.controller.dto.MoitDetailsResponse
import com.mashup.moit.moit.controller.dto.MoitInvitationCodeResponse
import com.mashup.moit.moit.controller.dto.MoitJoinRequest
import com.mashup.moit.moit.controller.dto.MoitJoinResponse
import com.mashup.moit.moit.controller.dto.MoitStudyListResponse
import com.mashup.moit.moit.controller.dto.MyMoitListResponse
import com.mashup.moit.moit.facade.MoitFacade
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Moit", description = "Moit 관련 Api 입니다.")
@RequestMapping("/api/v1/moit")
@RestController
class MoitController(
    private val moitFacade: MoitFacade
) {
    @Operation(summary = "Moit create API", description = "moit 생성")
    @PostMapping
    fun createMoit(
        @GetAuth userInfo: UserInfo,
        @Valid @RequestBody request: MoitCreateRequest,
    ): MoitApiResponse<MoitCreateResponse> {
        return MoitApiResponse.success(moitFacade.create(userInfo.id, request))
    }

    @Operation(summary = "Moit join API", description = "moit 가입 요청")
    @PostMapping("/join")
    fun joinMoit(
        @GetAuth userInfo: UserInfo,
        @RequestBody @Valid request: MoitJoinRequest,
    ): MoitApiResponse<MoitJoinResponse> {
        return MoitApiResponse.success(moitFacade.joinAsMember(userInfo.id, request.invitationCode))
    }

    @Operation(summary = "Moit Details API", description = "moit 상세 조회")
    @GetMapping("/{moitId}")
    fun getDetails(@PathVariable moitId: Long): MoitApiResponse<MoitDetailsResponse> {
        return MoitApiResponse.success(moitFacade.getMoitDetails(moitId))
    }

    @Operation(summary = "My Moit List API", description = "내 Moit List 조회")
    @GetMapping
    fun getMyMoits(
        @GetAuth userInfo: UserInfo,
    ): MoitApiResponse<MyMoitListResponse> {
        return MoitApiResponse.success(moitFacade.getMyMoits(userInfo.id))
    }

    @Operation(summary = "All attendances of all studies in Moit API", description = "Moit의 모든 스터디 출결 조회")
    @GetMapping("/{moitId}/attendance")
    fun getAllAttendances(@PathVariable moitId: Long): MoitApiResponse<MoitStudyListResponse> {
        return MoitApiResponse.success(MoitStudyListResponse.sample())
    }

    @Operation(summary = "Moit Invitation Code", description = "Moit의 가입 코드 조회")
    @GetMapping("/{moitId}/invitation-code")
    fun getMoitInvitationCode(@PathVariable moitId: Long): MoitApiResponse<MoitInvitationCodeResponse> {
        return MoitApiResponse.success(moitFacade.getInvitationCode(moitId))
    }
}
