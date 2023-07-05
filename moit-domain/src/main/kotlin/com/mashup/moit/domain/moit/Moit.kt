package com.mashup.moit.domain.moit

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

data class Moit(
    val id: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val invitationCode: String,
    val isEnd: Boolean,
    val scheduleDayOfWeeks: Set<DayOfWeek>,
    val scheduleStartDate: LocalDate,
    val scheduleEndDate: LocalDate,
    val scheduleRepeatCycle: ScheduleRepeatCycle,
    val scheduleStartTime: LocalTime,
    val scheduleEndTime: LocalTime,
    val fineLateTime: Int,
    val fineLateAmount: Long,
    val fineAbsenceTime: Int,
    val fineAbsenceAmount: Long,
    val notificationIsRemindActive: Boolean,
    val notificationRemindOption: NotificationRemindOption?,
    val notificationRemindLevel: NotificationRemindLevel?,
)

enum class ScheduleRepeatCycle(
    val period: Period?,
) {
    NONE(null),
    ONE_WEEK(Period.ofDays(7)),
    TWO_WEEK(Period.ofDays(14)),
    THREE_WEEK(Period.ofDays(21)),
    FOUR_WEEK(Period.ofDays(28)),
    ;
}

enum class NotificationRemindOption(
) {
    STUDY_DAY_10_AM, BEFORE_1_HOUR, BEFORE_30_MINUTE, BEFORE_10_MINUTE;
}

enum class NotificationRemindLevel {
    WEAK, NORMAL, HARD;
}
