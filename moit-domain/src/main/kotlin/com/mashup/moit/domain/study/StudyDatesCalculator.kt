package com.mashup.moit.domain.study

import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object StudyDatesCalculator {
    fun calculateStudyDates(
        dayOfWeeks: Set<DayOfWeek>,
        startDate: LocalDate,
        endDate: LocalDate,
        startTime: LocalTime,
        repeatCycle: ScheduleRepeatCycle,
    ): List<LocalDate> {
        val firstWeekDates = calculateFirstWeekStudyDates(
            dayOfWeeks = dayOfWeeks,
            startDate = startDate,
            startTime = startTime,
        )

        if (repeatCycle.period == null) return firstWeekDates.filter { !it.isAfter(endDate) }

        return generateSequence(firstWeekDates) { dates -> dates.map { it.plus(repeatCycle.period) } }
            .takeWhile { dates -> dates.any { !it.isAfter(endDate) } }
            .flatten()
            .filter { !it.isAfter(endDate) }.toList()
    }

    private fun calculateFirstWeekStudyDates(
        dayOfWeeks: Set<DayOfWeek>,
        startDate: LocalDate,
        startTime: LocalTime,
    ): List<LocalDate> {
        val now = LocalDateTime.now()
        return generateSequence(startDate) { date -> date.plusDays(1) }
            .filter { it.dayOfWeek in dayOfWeeks && LocalDateTime.of(it, startTime).isAfter(now) }
            .take(dayOfWeeks.size).toList()
    }
}
