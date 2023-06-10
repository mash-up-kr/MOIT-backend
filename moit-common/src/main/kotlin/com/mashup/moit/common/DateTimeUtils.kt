package com.mashup.moit.common

import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun LocalTime.formatLocalTime(): String {
    return this.format(TIME_FORMATTER)
}
