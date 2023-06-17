package com.mashup.moit.domain.study

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "study")
@Entity
class StudyEntity(
    @Column(name = "moit_id", nullable = false)
    val moitId: Long,

    @Column(name = "study_order", nullable = false)
    val order: Int,

    @Column(name = "start_at", nullable = false)
    val startAt: LocalDateTime,

    @Column(name = "end_at", nullable = false)
    val endAt: LocalDateTime,

    @Column(name = "remind_at")
    val remindAt: LocalDateTime?,

    @Column(name = "late_at", nullable = false)
    val lateAt: LocalDateTime,

    @Column(name = "absence_at", nullable = false)
    val absenceAt: LocalDateTime,
) : BaseEntity() {
    @Column(name = "first_attendance_user_id")
    var firstAttendanceUserId: Long? = null

    @Column(name = "attendance_code")
    var attendanceCode: String? = null

    fun toDomain() = Study(
        id = id,
        moitId = moitId,
        order = order,
    )
}
