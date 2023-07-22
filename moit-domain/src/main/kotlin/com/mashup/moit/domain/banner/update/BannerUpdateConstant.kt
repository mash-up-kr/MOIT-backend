package com.mashup.moit.domain.banner.update

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object BannerUpdateConstant {
    val BANNER_CLOSE_MAX_DATE: LocalDateTime = LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.of(23, 59, 59))
}
