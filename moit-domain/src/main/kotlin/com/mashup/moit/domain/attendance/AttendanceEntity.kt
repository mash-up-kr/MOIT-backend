package com.mashup.moit.domain.attendance

import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.study.StudyEntity
import com.mashup.moit.domain.user.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "attendance")
@Entity
class AttendanceEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    var study: StudyEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val status: Status,

    @Column(name = "attendance_at")
    val attendanceAt: LocalDateTime
) : BaseEntity()
