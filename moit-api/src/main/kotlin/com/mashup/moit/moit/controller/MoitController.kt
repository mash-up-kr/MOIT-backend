package com.mashup.moit.moit.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.moit.controller.dto.MoitJoinResponse
import com.mashup.moit.moit.facade.MoitFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
    @Operation(summary = "join Moit API", description = "moit 가입 요청 메서드")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "CREATED")])
    @PostMapping("/join/{invitationCode}")
    fun joinMoit(@Parameter(hidden = true) userId: Long, @PathVariable invitationCode: String): MoitApiResponse<MoitJoinResponse> {
        return MoitApiResponse.success(moitFacade.join(userId, invitationCode))
    }
}
