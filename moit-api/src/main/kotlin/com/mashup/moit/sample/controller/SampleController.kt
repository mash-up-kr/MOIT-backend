package com.mashup.moit.sample.controller

import com.mashup.moit.sample.controller.common.ApiResponse
import com.mashup.moit.sample.controller.dto.SampleCreateRequest
import com.mashup.moit.sample.controller.dto.SampleResponse
import com.mashup.moit.sample.service.SampleService
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/samples")
@RestController
class SampleController(
    private val sampleService: SampleService,
) {
    @PostMapping
    fun createSample(@RequestBody request: SampleCreateRequest): ApiResponse<SampleResponse> {
        return ApiResponse.success(
            sampleService.createSample(request.name).let { SampleResponse.of(it) }
        )
    }

    @GetMapping("/{sampleId}/nullable")
    fun findBySampleIdOrNull(@PathVariable sampleId: Long): ApiResponse<SampleResponse?> {
        return ApiResponse.success(
            sampleService.findBySampleIdOrNull(sampleId)?.let { SampleResponse.of(it) }
        )
    }

    @GetMapping("/{sampleId}/not-null")
    fun findBySampleId(@PathVariable sampleId: Long): ApiResponse<SampleResponse> {
        return ApiResponse.success(
            sampleService.findBySampleId(sampleId).let { SampleResponse.of(it) }
        )
    }
}
