package com.mashup.moit.domain.moit

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.LocalTime

@Embeddable
data class FinePolicy(
    @Column(name = "late_time_of_fine") val lateTime: Int,
    @Column(name = "late_amount_of_fine") val lateAmount: Int,
    @Column(name = "absence_time_of_fine") val absenceTime: Int,
    @Column(name = "absence_amount_of_fine") val absenceAmount: Int
)
