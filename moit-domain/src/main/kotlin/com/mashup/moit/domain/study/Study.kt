package com.mashup.moit.domain.study

import java.time.LocalDateTime

data class Study(
    val id: Long,
    val moitId: Long,
    val order: Int,
    val startAt: LocalDateTime,
)
