package com.mashup.moit.domain.notification

import java.time.LocalDateTime

sealed interface NotificationEvent

data class AttendanceStartNotificationEvent(
    val studyIdWithMoitIds: Set<Pair<Long, Long>>,
    val flushAt: LocalDateTime
) : NotificationEvent


