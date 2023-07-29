package com.mashup.moit.facade

import com.mashup.moit.domain.sample.SampleService
import com.mashup.moit.controller.sample.dto.SampleCreateRequest
import com.mashup.moit.controller.sample.dto.SampleResponse
import org.springframework.stereotype.Component

@Component
class SampleFacade(
    private val sampleService: SampleService,
) {
    fun createSample(request: SampleCreateRequest): SampleResponse {
        return sampleService.createSample(request.name).let { SampleResponse.of(it) }
    }

    fun findBySampleIdOrNull(sampleId: Long): SampleResponse? {
        return sampleService.findBySampleIdOrNull(sampleId)?.let { SampleResponse.of(it) }
    }

    fun findBySampleId(sampleId: Long): SampleResponse {
        return sampleService.findBySampleId(sampleId).let { SampleResponse.of(it) }
    }
}
