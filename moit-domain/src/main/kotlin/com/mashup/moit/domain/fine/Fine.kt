package com.mashup.moit.domain.fine

import com.mashup.moit.domain.attendance.AttendanceStatus
import java.time.LocalDateTime

enum class FineApproveStatus {
    NEW, IN_PROGRESS, REJECTED, APPROVED;

    companion object {
        val UNAPPROVED_STATUSES = setOf(NEW, IN_PROGRESS, REJECTED)
    }
}

data class Fine(
    val id: Long,
    val moitId: Long,
    val studyId: Long,
    val userId: Long,
    val attendanceStatus: AttendanceStatus,
    val amount: Long,
    val isApproved: Boolean,
    val approveStatus: FineApproveStatus,
    val approvedAt: LocalDateTime?,
    val paymentImageUrl: String?,
)
