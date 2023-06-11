package com.mashup.moit.common.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    fun LocalTime.responseFormatTime(): String {
        return this.format(TIME_FORMATTER)
    }
}
