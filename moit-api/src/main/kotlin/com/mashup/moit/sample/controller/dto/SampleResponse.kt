package com.mashup.moit.sample.controller.dto

import com.mashup.moit.domain.sample.Sample

data class SampleResponse(
    val id: Long,
    val name: String,
) {
    companion object {
        fun of(sample: Sample) = SampleResponse(
            id = sample.id,
            name = sample.name,
        )
    }
}
