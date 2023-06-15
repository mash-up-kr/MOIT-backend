package com.mashup.moit.study.controller.dto

import com.mashup.moit.domain.attendance.AttendanceStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Study 참석 코드 요청")
data class StudyAttendanceCodeRequest(
<<<<<<< HEAD
    @Schema(description = "Study 참석 코드")
    @field:NotBlank
    @Size(min = 4, max = 4)
=======
    @Schema(description = "유저 id")
    val userId: Long,

    @Schema(description = "Study 참석 코드")
    @field:NotBlank
    @Size(min = 8, max = 8)
>>>>>>> 2a0e340 (Add registerAttendnaceKeyword API to StudyController)
    val attendanceKeyword: String
)

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
