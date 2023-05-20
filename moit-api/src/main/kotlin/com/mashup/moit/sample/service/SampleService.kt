package com.mashup.moit.sample.service

import com.mashup.moit.domain.sample.Sample
import com.mashup.moit.domain.sample.SampleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class SampleService(
    private val sampleRepository: SampleRepository,
) {
    fun createSample(name: String): Sample {
        return sampleRepository.save(Sample(name = name))
    }

    fun findBySampleIdOrNull(sampleId: Long): Sample? {
        return sampleRepository.findByIdOrNull(sampleId)
    }
}
