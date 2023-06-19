package com.mashup.moit.study.controller.dto

import com.mashup.moit.domain.attendance.AttendanceStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Study 상세 조회 응답")
data class StudyDetailsResponse(
    @Schema(description = "Study가 속한 Moit 이름")
    val moitName: String,
    @Schema(description = "Study 회차")
    val order: Int,
    @Schema(description = "Study 시작 날짜 및 시간")
    val startAt: LocalDateTime,
    @Schema(description = "Study 끝나는 날짜 및 시간")
    val endAt: LocalDateTime,
    @Schema(description = "Study 리마인드 날짜 및 시간")
    val remindAt: LocalDateTime,
    @Schema(description = "Study 지각 날짜 및 시간")
    val lateAt: LocalDateTime,
    @Schema(description = "Study 결석 날짜 및 시간")
    val absenceAt: LocalDateTime,
    @Schema(description = "첫 번째 출석한 유저 아이디")
    val firstAttendanceUserNickname: String? = null
) {
    companion object {
        fun sample(): StudyDetailsResponse = StudyDetailsResponse(
            moitName = "전자군단",
            order = 1,
            startAt = LocalDateTime.of(2023, 6, 15, 16, 0),
            endAt = LocalDateTime.of(2023, 6, 15, 19, 0),
            remindAt = LocalDateTime.of(2023, 6, 15, 15, 30),
            lateAt = LocalDateTime.of(2023, 6, 15, 16, 30),
            absenceAt = LocalDateTime.of(2023, 6, 15, 17, 0),
            firstAttendanceUserNickname = null
        )
    }
}

@Schema(description = "Study 참석 코드 요청")
data class StudyAttendanceKeywordRequest(
    @Schema(description = "Study 참석 코드")
    @field:NotBlank
    @Size(min = 4, max = 4)
    val attendanceKeyword: String
)

@Schema(description = "Study 출석 키워드 조회 응답")
data class StudyAttendanceKeywordResponse(
    @Schema(description = "Study 출석 키워드 (null일 경우 미등록 상태)")
    val attendanceKeyword: String?,
) {
    companion object {
        fun of(keyword: String?) = StudyAttendanceKeywordResponse(attendanceKeyword = keyword)
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
