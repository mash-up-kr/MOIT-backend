package com.mashup.moit.infra.event

fun interface MoitEvent {
    fun getTopic(): String
}

data class StudyCreateEvent(val moitId: Long?) : MoitEvent {
    override fun getTopic(): String {
        return STUDY_CREATE_TOPIC
    }
}
