package com.mashup.moit.domain.moit

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Embeddable
data class SchedulePolicy(
    @Enumerated(EnumType.STRING) @Column(name = "day_of_weeks_of_schedule") val dayOfWeeks: Set<DayOfWeek>,
    @Column(name = "start_time_of_schedule") val startTime: LocalTime,
    @Column(name = "end_time_of_schedule") val endTime: LocalTime,
    @Enumerated(EnumType.STRING) @Column(name = "repeat_cycle_of_schedule") val repeatCycle: RepeatCycle,
    @Column(name = "start_date_of_schedule") val startDate: LocalDate,
    @Column(name = "end_date_of_schedule") val endDate: LocalDate
)

// 반복 주기로 앞에서부터 매주, 2주, 3주, 4주
enum class RepeatCycle {
    ONE, TWO, THREE, FOUR
}
