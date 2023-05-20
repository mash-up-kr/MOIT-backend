package com.mashup.moit.sample.controller

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
    fun createSample(@RequestBody request: SampleCreateRequest): SampleResponse {
        return sampleService.createSample(request.name).let { SampleResponse.of(it) }
    }


    @GetMapping("/{sampleId}")
    fun findBySampleIdOrNull(@PathVariable sampleId: Long): SampleResponse? {
        return sampleService.findBySampleIdOrNull(sampleId)?.let { SampleResponse.of(it) }
    }
}
