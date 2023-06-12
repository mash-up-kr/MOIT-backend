package com.mashup.moit.fine.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.fine.facade.FineConfirmFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "FineConfirm", description = "벌금 인증 관련 Api 입니다.")
@RequestMapping("/api/v1/moit")
@RestController
class FineConfirmController(
    private val fineConfirmFacade: FineConfirmFacade
) {

    @Operation(summary = "벌금 인증 요청 API", description = "벌금 송금 인증을 요청하는 API - Image 업로드가 필요함")
    @PostMapping(
        "/{moitId}/fine/{fineId}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun requestConfirmFine(
        @PathVariable("moitId") moitId: Long,
        @PathVariable("fineId") fineId: Long,
        @RequestParam("fineRemittanceImage") fineRemittanceImage: MultipartFile
    ): MoitApiResponse<Unit> {
        return MoitApiResponse.success()
    }

}
