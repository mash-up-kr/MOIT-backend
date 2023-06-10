package com.mashup.moit.domain.fine

import com.mashup.moit.domain.attendance.AttendanceEntity
import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.moit.MoitEntity
import com.mashup.moit.domain.user.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "fine")
@Entity
class FineEntity(
    @OneToOne
    @JoinColumn(name = "attendance_id", nullable = false)
    var attendance: AttendanceEntity,

    @Enumerated(EnumType.STRING)
    @Column(name = "approve_status", nullable = false)
    val approveStatus: FineApproveStatus = FineApproveStatus.NEW,

    @Column(name = "approved_at")
    val approvedAt: LocalDateTime?,

    @Column(name = "approve_image_url")
    val approveImageUrl: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moit_id", nullable = false)
    val moit: MoitEntity
) : BaseEntity()
