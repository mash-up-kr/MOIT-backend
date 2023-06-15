package com.mashup.moit.study.controller.dto

import com.mashup.moit.domain.attendance.AttendanceStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Study 출석 키워드 조회 응답")
data class StudyAttendanceKeywordResponse(
    @Schema(description = "Study 출석 키워드")
    val attendanceKeyword: String,
) {
    companion object {
        fun sample() = StudyAttendanceKeywordResponse("벌금환영")
    }
}

@Schema(description = "Study 첫 출석자 존재 유무 응답")
data class StudyFirstAttendanceResponse(
    @Schema(description = "Study 첫 출석자 존재 유무")
    val isFirstAttendance: Boolean,
) {
    companion object {
        fun sample() = StudyFirstAttendanceResponse(true)
    }
}

@Schema(description = "Study 유저 출석상태 응답")
data class StudyUserAttendanceStatusResponse(
    val userId: Long,
    val nickname: String,
    val profileImage: Int,
    val attendanceStatus: AttendanceStatus,
    val attendanceAt: LocalDateTime?
) {
    companion object {
        fun sample() = StudyUserAttendanceStatusResponse(
            1L,
            "나는 부자가 될테야",
            2,
            AttendanceStatus.ATTENDANCE,
            LocalDateTime.now()
        )
    }
}
