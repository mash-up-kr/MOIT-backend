package com.mashup.moit.infra.event

import java.time.LocalDateTime

fun interface MoitEvent {
    fun getTopic(): String
}

data class MoitCreateEvent(val moitId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.MOIT_CREATE
    }
}

data class MoitJoinEvent(val moitId: Long, val userId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.MOIT_JOIN
    }
}

data class StudyAttendanceEvent(val attendanceId: Long, val moitId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.STUDY_ATTENDANCE
    }
}

data class StudyAttendanceEventBulk(val attendanceIdWithMoitIds: List<Pair<Long, Long>>) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.STUDY_ATTENDANCE_BULK
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

data class FineCreateEventBulk(val fineIds: Set<Long>) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.FINE_CREATE_BULK
    }
}

data class FineApproveEvent(val fineId: Long) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.FINE_APPROVE
    }
}

data class StudyAttendanceStartNotificationPushEvent(val studyIdWithMoitIds: Set<Pair<Long, Long>>, val flushAt: LocalDateTime) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.STUDY_ATTENDANCE_START_NOTIFICATION
    }
}
