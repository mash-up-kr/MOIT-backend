package com.mashup.moit.domain.moit

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class FinePolicyColumns(
    @Column(name = "late_time", nullable = false)
    val lateTime: Int,

    @Column(name = "late_amount", nullable = false)
    val lateAmount: Int,

    @Column(name = "absence_time", nullable = false)
    val absenceTime: Int,

    @Column(name = "absence_amount", nullable = false)
    val absenceAmount: Int,
)
