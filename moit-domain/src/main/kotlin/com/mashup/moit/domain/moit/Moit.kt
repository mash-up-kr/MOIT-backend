package com.mashup.moit.domain.moit

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

enum class ScheduleRepeatCycle {
    NONE, ONE_WEEK, TWO_WEEK, THREE_WEEK, FOUR_WEEK;
}

enum class NotificationRemindOption {
    BEFORE_TEN_MINUTE, BEFORE_TWENTY_MINUTE, AFTER_TEN_MINUTE;
}

enum class NotificationRemindLevel {
    WEAK, NORMAL, HARD;
}

data class Moit(
    val id: Long,
    val name: String,
    val description: String?,
    val invitationCode: String,
    val isEnd: Boolean,
    val scheduleDayOfWeeks: Set<DayOfWeek>,
    val scheduleRepeatCycle: ScheduleRepeatCycle,
    val scheduleStartTime: LocalTime,
    val scheduleEndTime: LocalTime,
    val fineLateTime: Int,
    val fineLateAmount: Int,
    val fineAbsenceTime: Int,
    val fineAbsenceAmount: Int,
    val notificationIsRemindActive: Boolean,
    val notificationRemindOption: NotificationRemindOption?,
    val notificationRemindLevel: NotificationRemindLevel?,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
