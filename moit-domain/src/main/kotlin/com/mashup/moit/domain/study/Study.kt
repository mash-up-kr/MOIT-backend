package com.mashup.moit.domain.study

import java.time.LocalDateTime

data class Study(
    val id: Long,
    val moitId: Long,
    val order: Int,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val remindAt: LocalDateTime?,
    val lateAt: LocalDateTime,
    val absenceAt: LocalDateTime,
    val firstAttendanceUserId: Long? = null,
    val isInitialized: Boolean,
)
