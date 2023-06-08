package com.mashup.moit.common.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun formatLocalTime(localTime: LocalTime): String {
        return localTime.format(TIME_FORMATTER)
    }
}
