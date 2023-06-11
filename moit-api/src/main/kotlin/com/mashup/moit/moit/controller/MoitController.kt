package com.mashup.moit.moit.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.moit.controller.dto.MoitDetailsResponse
import com.mashup.moit.moit.controller.dto.MoitJoinRequest
import com.mashup.moit.moit.controller.dto.MoitJoinResponse
import com.mashup.moit.moit.facade.MoitFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Moit", description = "Moit 관련 Api 입니다.")
@RequestMapping("/api/v1/moit")
@RestController
class MoitController(
    private val moitFacade: MoitFacade
) {
    @Operation(summary = "Moit join API", description = "moit 가입 요청")
    @PostMapping("/join")
    fun joinMoit(
        @Parameter(hidden = true) userId: Long,
        @RequestBody(description = "초대코드") @Valid request: MoitJoinRequest
    ): MoitApiResponse<MoitJoinResponse> {
        return MoitApiResponse.success(moitFacade.join(userId, request.invitationCode))
    }

    @Operation(summary = "Moit Details API", description = "moit 상세 조회")
    @GetMapping("/{moitId}")
    fun getDetails(@Parameter(hidden = true) userId: Long, @PathVariable moitId: Long): MoitApiResponse<MoitDetailsResponse> {
        return MoitApiResponse.success(MoitDetailsResponse.sample())
    }
}
