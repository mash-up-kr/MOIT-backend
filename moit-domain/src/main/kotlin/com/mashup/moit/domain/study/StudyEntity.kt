package com.mashup.moit.domain.study

import com.mashup.moit.domain.attendance.AttendanceEntity
import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.moit.MoitEntity
import com.mashup.moit.domain.user.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "study")
@Entity
class StudyEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moit_id", nullable = false)
    val moit: MoitEntity,

    @Column(name = "study_order", nullable = false)
    val order: Int,

    @Column(name = "start_at", nullable = false)
    val startAt: LocalDateTime,

    @Column(name = "end_at", nullable = false)
    val endAt: LocalDateTime,

    @Column(name = "remind_at", nullable = false)
    val remindAt: LocalDateTime,

    @Column(name = "late_at", nullable = false)
    val lateAt: LocalDateTime,

    @Column(name = "absence_at", nullable = false)
    val absenceAt: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "first_attendance_user_id")
    val firstAttendanceUser: UserEntity?,

    @Column(name = "attendance_code")
    val attendanceCode: String?,
) : BaseEntity() {
    @OneToMany(mappedBy = "study")
    val attendances: List<AttendanceEntity> = emptyList()
}
