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

data class StudyAttendanceEvent(val attendanceId: Long, val moitId: Long) : StudyEvent()
data class StudyAttendanceEventBulk(val attendanceIdWithMoitIds: List<Pair<Long, Long>>) : StudyEvent()
data class StudyInitializeEvent(val studyId: Long) : StudyEvent()
abstract class StudyEvent : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.STUDY
    }
}

data class FineCreateEvent(val fineId: Long) : FineEvent()
data class FineCreateEventBulk(val fineIds: Set<Long>) : FineEvent()
data class FineApproveEvent(val fineId: Long) : FineEvent()
abstract class FineEvent : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.FINE
    }
}

data class StudyAttendanceStartNotificationPushEvent(
    val studyIdWithMoitIds: Set<Pair<Long, Long>>,
    override val flushAt: LocalDateTime
) : NotificationPushEvent(flushAt)

data class RemindFineNotificationPushEvent(
    val fineIds: Set<Long>,
    override val flushAt: LocalDateTime
) : NotificationPushEvent(flushAt)

data class ScheduledStudyNotificationPushEvent(
    val studyIdWithMoitIds: Set<Pair<Long, Long>>,
    override val flushAt: LocalDateTime
) : NotificationPushEvent(flushAt)
abstract class NotificationPushEvent(
    open val flushAt: LocalDateTime
) : MoitEvent {
    override fun getTopic(): String {
        return KafkaEventTopic.NOTIFICATION
    }
}
