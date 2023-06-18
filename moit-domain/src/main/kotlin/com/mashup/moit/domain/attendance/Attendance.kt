package com.mashup.moit.domain.attendance

import java.time.LocalDateTime

data class Attendance(
    val studyId: Long,
    val userId: Long,
    val status: AttendanceStatus,
    val attendanceAt: LocalDateTime?,
)

enum class AttendanceStatus {
    UNDECIDED, ATTENDANCE, LATE, ABSENCE;
}
