package com.mashup.moit.domain.attendance

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "attendance")
@Entity
class AttendanceEntity(
    @Column(name = "study_id", nullable = false)
    val studyId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: AttendanceStatus,

    @Column(name = "attendance_at")
    var attendanceAt: LocalDateTime?,
) : BaseEntity() {
    fun toDomain() = Attendance(
        id = id,
        studyId = studyId,
        userId = userId,
        status = status,
        attendanceAt = attendanceAt,
    )
}
