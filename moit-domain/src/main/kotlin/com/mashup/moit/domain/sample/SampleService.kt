package com.mashup.moit.domain.sample

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SampleService(
    private val sampleRepository: SampleRepository,
) {
    @Transactional
    fun createSample(name: String): Sample {
        return sampleRepository.save(SampleEntity(name = name)).toDomain()
    }

    fun findBySampleIdOrNull(sampleId: Long): Sample? {
        return sampleRepository.findByIdOrNull(sampleId)?.toDomain()
    }

    fun findBySampleId(sampleId: Long): Sample {
        return findBySampleIdOrNull(sampleId) ?: throw MoitException.of(MoitExceptionType.NOT_EXIST)
    }
}
