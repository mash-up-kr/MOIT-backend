package com.mashup.moit.domain.notification

import java.time.LocalDateTime

sealed interface NotificationEvent

data class AttendanceStartNotificationEvent(
    val studyIdWithMoitIds: Set<Pair<Long, Long>>,
    val flushAt: LocalDateTime
) : NotificationEvent

data class RemindFineNotificationEvent(
    val fineIds: Set<Long>,
) : NotificationEvent

data class ScheduledStudyNotificationEvent(
    val studyIdWithMoitIds: Set<Pair<Long, Long>>,
    val flushAt: LocalDateTime
) : NotificationEvent
