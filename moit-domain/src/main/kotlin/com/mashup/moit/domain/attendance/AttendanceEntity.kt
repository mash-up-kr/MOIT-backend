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
    var userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: AttendanceStatus,

    @Column(name = "attendance_at")
    val attendanceAt: LocalDateTime?,
) : BaseEntity()
