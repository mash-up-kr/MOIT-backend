package com.mashup.moit.infra.event

fun interface MoitEvent {
    fun getTopic(): String
}

data class MoitCreateEvent(val moitId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.MOIT_CREATE
    }
}

data class StudyAttendanceEvent(val attendanceId: Long, val moitId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.STUDY_ATTENDANCE
    }
}

data class StudyInitializeEvent(val studyId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.STUDY_INITIALIZE
    }
}

data class FineCreateEvent(val fineId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.FINE_CREATE
    }
}

data class FineApproveEvent(val fineId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.FINE_APPROVE
    }
}
