package com.mashup.moit.moit.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.moit.controller.dto.MoitDetailsResponse
import com.mashup.moit.moit.controller.dto.MoitJoinRequest
import com.mashup.moit.moit.controller.dto.MoitJoinResponse
import com.mashup.moit.moit.facade.MoitFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "Moit", description = "Moit 관련 Api 입니다.")
@RequestMapping("/api/v1/moit")
@RestController
class MoitController(
    private val moitFacade: MoitFacade
) {
    @Operation(summary = "Moit join API", description = "moit 가입 요청")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "SUCCESS")])
    @PostMapping("/join")
    fun joinMoit(
        @Parameter(hidden = true) userId: Long,
        @RequestBody(description = "초대코드") @Valid request: MoitJoinRequest
    ): MoitApiResponse<MoitJoinResponse> {
        return MoitApiResponse.success(moitFacade.join(userId, request.invitationCode))
    }

    @Operation(summary = "Moit Details API", description = "moit 상세 조회")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "SUCCESS")])
    @GetMapping("/{moitId}")
    fun getDetails(@Parameter(hidden = true) userId: Long, @PathVariable moitId: Long): MoitApiResponse<MoitDetailsResponse> {
        return MoitApiResponse.success(MoitDetailsResponse.sample())
    }
}
