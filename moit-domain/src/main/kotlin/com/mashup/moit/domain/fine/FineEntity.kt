package com.mashup.moit.domain.fine

import com.mashup.moit.domain.attendance.AttendanceEntity
import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "fine")
@Entity
class FineEntity(
    @OneToOne
    @JoinColumn(name = "attendance_id", nullable = false)
    var attendance: AttendanceEntity,

    @Column(name = "amount", nullable = false)
    val amount: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "approve_status", nullable = false)
    var approveStatus: FineApproveStatus = FineApproveStatus.NEW,

    @Column(name = "approved_at")
    val approvedAt: LocalDateTime?,

    @Column(name = "approve_image_url")
    val approveImageUrl: String?,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "moit_id", nullable = false)
    val moitId: Long,
    @Column(name = "study_id", nullable = false)
    val studyId: Long,
) : BaseEntity() {
    fun toDomain(): Fine {
        val isApproved = when (approveStatus) {
            FineApproveStatus.APPROVED -> true
            else -> false
        }

        return Fine(
            id = id,
            moitId = moitId,
            studyId = attendance.studyId,
            userId = userId,
            attendanceStatus = attendance.status,
            amount = amount,
            isApproved = isApproved,
            approveStatus = approveStatus,
            approvedAt = approvedAt,
            approveImageUrl = approveImageUrl
        )
    }
}
