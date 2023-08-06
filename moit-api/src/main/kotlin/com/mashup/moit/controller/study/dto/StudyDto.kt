package com.mashup.moit.controller.study.dto

import com.mashup.moit.domain.attendance.Attendance
import com.mashup.moit.domain.attendance.AttendanceStatus
import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.study.Study
import com.mashup.moit.domain.user.User
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
    val remindAt: LocalDateTime? = null,
    @Schema(description = "Study 지각 날짜 및 시간")
    val lateAt: LocalDateTime,
    @Schema(description = "Study 결석 날짜 및 시간")
    val absenceAt: LocalDateTime,
    @Schema(description = "첫 번째 출석한 유저 아이디")
    val firstAttendanceUserNickname: String? = null
) {
    companion object {
        fun of(
            moit: Moit,
            study: Study,
            firstAttendanceUser: User?
        ): StudyDetailsResponse = StudyDetailsResponse(
            moitName = moit.name,
            order = study.order,
            startAt = study.startAt,
            endAt = study.endAt,
            remindAt = study.remindAt,
            lateAt = study.lateAt,
            absenceAt = study.absenceAt,
            firstAttendanceUserNickname = firstAttendanceUser?.nickname
        )
    }
}

@Schema(description = "Study 참석 코드 요청")
data class StudyAttendanceKeywordRequest(
    @Schema(description = "Study 참석 코드")
    @field:NotBlank
    @field:Size(min = 4, max = 4)
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
        fun of(isFirstAttendance: Boolean) = StudyFirstAttendanceResponse(isFirstAttendance)
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
        fun of(attendance: Attendance, user: User) = StudyUserAttendanceStatusResponse(
            userId = user.id,
            nickname = user.nickname,
            profileImage = user.profileImage,
            attendanceStatus = attendance.status,
            attendanceAt = attendance.attendanceAt,
        )
    }
}

@Schema(description = "Study 결석 처리 요청")
data class StudyAdjustAbsenceRequest(
    @Schema(description = "Study Ids")
    @field:NotBlank
    val studyIds: List<Long>
)
