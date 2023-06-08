package com.mashup.moit.domain.moit

import com.mashup.moit.domain.moit.converter.DayOfWeeksConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Embeddable
data class SchedulePolicyColumns(
    @Convert(converter = DayOfWeeksConverter::class)
    @Column(name = "day_of_weeks", nullable = false)
    val dayOfWeeks: Set<DayOfWeek>,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_cycle", nullable = false)
    val repeatCycle: ScheduleRepeatCycle,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalTime,
)
