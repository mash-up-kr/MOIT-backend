package com.mashup.moit.sample.controller

import com.mashup.moit.common.ApiResponse
import com.mashup.moit.sample.controller.dto.SampleCreateRequest
import com.mashup.moit.sample.controller.dto.SampleResponse
import com.mashup.moit.sample.facade.SampleFacade
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/samples")
@RestController
class SampleController(
    private val sampleFacade: SampleFacade,
) {
    @PostMapping
    fun createSample(@RequestBody request: SampleCreateRequest): ApiResponse<SampleResponse> {
        return ApiResponse.success(sampleFacade.createSample(request))
    }

    @GetMapping("/{sampleId}/nullable")
    fun findBySampleIdOrNull(@PathVariable sampleId: Long): ApiResponse<SampleResponse?> {
        return ApiResponse.success(sampleFacade.findBySampleIdOrNull(sampleId))
    }

    @GetMapping("/{sampleId}/not-null")
    fun findBySampleId(@PathVariable sampleId: Long): ApiResponse<SampleResponse> {
        return ApiResponse.success(sampleFacade.findBySampleId(sampleId))
    }
}
