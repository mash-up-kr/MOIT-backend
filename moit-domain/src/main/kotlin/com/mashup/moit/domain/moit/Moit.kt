package com.mashup.moit.domain.moit

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

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
    val fineLateAmount: Int,
    val fineAbsenceTime: Int,
    val fineAbsenceAmount: Int,
    val notificationIsRemindActive: Boolean,
    val notificationRemindOption: NotificationRemindOption?,
    val notificationRemindLevel: NotificationRemindLevel?,
)

enum class ScheduleRepeatCycle(
    val duration: Duration?,
) {
    NONE(null),
    ONE_WEEK(Duration.ofDays(7)),
    TWO_WEEK(Duration.ofDays(14)),
    FOUR_WEEK(Duration.ofDays(28)),
    ;
}

enum class NotificationRemindOption(
) {
    STUDY_DAY_10_AM, BEFORE_1_HOUR, BEFORE_30_MINUTE, BEFORE_10_MINUTE;
}

enum class NotificationRemindLevel {
    WEAK, NORMAL, HARD;
}
