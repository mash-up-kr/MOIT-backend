package com.mashup.moit.controller.sample

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.controller.sample.dto.SampleCreateRequest
import com.mashup.moit.controller.sample.dto.SampleResponse
import com.mashup.moit.facade.SampleFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Sample", description = "Sample Api 입니다.")
@RequestMapping("/api/v1/samples")
@RestController
class SampleController(
    private val sampleFacade: SampleFacade,
) {
    @Operation(summary = "create API", description = "sample 생성 요청 메서드")
    @PostMapping
    fun createSample(@RequestBody request: SampleCreateRequest): MoitApiResponse<SampleResponse> {
        return MoitApiResponse.success(sampleFacade.createSample(request))
    }

    @GetMapping("/{sampleId}/nullable")
    fun findBySampleIdOrNull(@PathVariable sampleId: Long): MoitApiResponse<SampleResponse?> {
        return MoitApiResponse.success(sampleFacade.findBySampleIdOrNull(sampleId))
    }

    @GetMapping("/{sampleId}/not-null")
    fun findBySampleId(@PathVariable sampleId: Long): MoitApiResponse<SampleResponse> {
        return MoitApiResponse.success(sampleFacade.findBySampleId(sampleId))
    }
}
