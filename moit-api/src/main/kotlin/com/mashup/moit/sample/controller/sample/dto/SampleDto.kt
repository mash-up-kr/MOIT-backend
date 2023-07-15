package com.mashup.moit.sample.controller.sample.dto

import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.sample.Sample

data class SampleCreateRequest(
    val name: String,
)

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

data class SampleNotificationRequest(
    val studyId: Long,
    val studyName: String,
    val remainMinutes: NotificationRemindOption,
) {
    companion object {
        fun sample() = SampleNotificationRequest(
            studyId = 1L,
            studyName = "전자군단",
            remainMinutes = NotificationRemindOption.BEFORE_10_MINUTE
        )
    }
}
