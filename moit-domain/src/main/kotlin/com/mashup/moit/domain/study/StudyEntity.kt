package com.mashup.moit.domain.study

import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.moit.MoitEntity
import com.mashup.moit.domain.user.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "study")
@Entity
class StudyEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moit_id")
    var moit: MoitEntity?,

    @OneToOne
    @JoinColumn(name = "first_attendance_user_id")
    val firstAttendanceUser: UserEntity,

    @Column(name = "study_order")
    val studyOrder: Int,

    @Column(name = "start_date_time")
    val startDateTime: LocalDateTime,

    @Column(name = "end_date_time")
    val endDateTime: LocalDateTime,

    @Column(name = "remind_date_time")
    val remindDateTime: LocalDateTime,

    @Column(name = "late_date_time")
    val lateDateTime: LocalDateTime,

    @Column(name = "absence_date_time")
    val absenceDateTime: LocalDateTime,

    @Column(name = "attendance_code")
    val attendanceCode: String,

    @Column(name = "total_member_count")
    val totalMemberCount: Int,

    @Column(name = "attendance_member_count")
    val attendanceMemberCount: Int,

    @Column(name = "late_member_count")
    val lateMemberCount: Int,

    @Column(name = "absence_member_count")
    val absenceMemberCount: Int
) : BaseEntity()
