package com.mashup.moit.domain.study

import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class StudyDatesCalculator {
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

        if (repeatCycle.duration == null) return firstWeekDates.filter { !it.isAfter(endDate) }

        return generateSequence(firstWeekDates) { dates -> dates.map { it.plus(repeatCycle.duration) } }
            .takeWhile { dates -> dates.any { !it.isAfter(endDate) } }
            .flatten()
            .filter { !it.isAfter(endDate) }.toList()
    }

    private fun calculateFirstWeekStudyDates(
        dayOfWeeks: Set<DayOfWeek>,
        startDate: LocalDate,
        startTime: LocalTime,
    ): List<LocalDate> {
        return generateSequence(startDate) { date -> date.plusDays(1) }
            .filter { date ->
                date.dayOfWeek in dayOfWeeks
                        && LocalDateTime.of(date, startTime).isAfter(LocalDateTime.now())
            }
            .take(dayOfWeeks.size).toList()
    }


    // 오늘: 목요일
    // 스터디 날짜: 월, 목
    // 반복 주기: 2주

    // 월 화 수 목(오늘, 스터디) 금 토 일
    // 월(스터디) 화 수 목 금 토 일
    // 월 화 수 목(스터디) 금 토 일
    // 월(스터디) 화 수 목 금 토 일

//    fun calculate(
//        dayOfWeeks: Set<DayOfWeek>,
//        startDate: LocalDate,
//        endDate: LocalDate,
//        startTime: LocalTime,
//        repeatCycle: ScheduleRepeatCycle,
//    ): List<LocalDate> {
//        val studyDates: MutableList<LocalDate> = mutableListOf()
//        val startWeekDates = emptyList<LocalDate>()
//
//        if (repeatCycle.numberOfDays == null) return startWeekDates
//        var dates: List<LocalDate> = startWeekDates
//        while (true) {
//            dates = dates.map { it.plusDays(repeatCycle.numberOfDays) }
//            studyDates.addAll(dates)
//            if (dates.any { it.isAfter(endDate) }) {
//                break
//            }
//        }
//        return studyDates.filter { !it.isAfter(endDate) }
//    }
}

