package com.mashup.moit.moit.controller.dto

import com.mashup.moit.domain.attendance.AttendanceStatus
import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Schema(description = "moit 생성 RequestBody")
data class MoitCreateRequest(
    @field:NotBlank
    val name: String,
    val description: String?,
    @field:NotEmpty
    val dayOfWeeks: Set<DayOfWeek>,
    @field:NotNull
    val startDate: LocalDate,
    @field:NotNull
    val endDate: LocalDate,
    @field:NotNull
    val repeatCycle: ScheduleRepeatCycle,
    @field:NotNull
    val startTime: LocalTime,
    @field:NotNull
    val endTime: LocalTime,
    @field:NotNull
    @field:PositiveOrZero
    val lateTime: Int,
    @field:NotNull
    @field:PositiveOrZero
    val lateAmount: Long,
    @field:NotNull
    @field:PositiveOrZero
    val absenceTime: Int,
    @field:NotNull
    @field:PositiveOrZero
    val absenceAmount: Long,
    @field:NotNull
    val isRemindActive: Boolean,
    val remindOption: NotificationRemindOption?,
)

data class MoitCreateResponse(
    @Schema(description = "생성된 moitId")
    val moitId: Long,
    @Schema(description = "moit 초대 코드")
    @field:NotBlank
    @Size(min = 6, max = 6)
    val invitationCode: String,
)

@Schema(description = "moit 가입 RequestBody")
data class MoitJoinRequest(
    @Schema(description = "유저 id")
    val userId: Long,

    @Schema(description = "moit 초대 코드")
    @field:NotBlank
    @Size(min = 6, max = 6)
    val invitationCode: String,
)

@Schema(description = "moit 가입 응답")
data class MoitJoinResponse(
    @Schema(description = "가입한 moitId")
    val moitId: Long
) {
    companion object {
        fun of(moitId: Long) = MoitJoinResponse(moitId)
    }
}

@Schema(description = "moit 상세 조회 응답")
data class MoitDetailsResponse(
    @Schema(description = "moitId")
    val moitId: Long,
    @Schema(description = "moit 이름")
    val name: String,
    @Schema(description = "moit장 Id")
    val masterId: Long,
    @Schema(description = "moit 설명")
    val description: String?,
    @Schema(description = "moit image url")
    val imageUrl: String?,
    @Schema(description = "moit 반복 요일")
    val scheduleDayOfWeeks: Set<DayOfWeek>,
    @Schema(description = "moit 반복 주기")
    val scheduleRepeatCycle: ScheduleRepeatCycle,
    @Schema(description = "moit 시작 시간", example = "HH:mm")
    val scheduleStartTime: LocalTime,
    @Schema(description = "moit 종료 시간", example = "HH:mm")
    val scheduleEndTime: LocalTime,
    @Schema(description = "moit 지각 시간")
    val fineLateTime: Int,
    @Schema(description = "moit 지각 벌금")
    val fineLateAmount: Long,
    @Schema(description = "moit 결석 시간")
    val fineAbsenceTime: Int,
    @Schema(description = "moit 결석 벌금")
    val fineAbsenceAmount: Long,
    @Schema(description = "moit 알람 리마인드 on/off")
    val notificationIsRemindActive: Boolean,
    @Schema(description = "moit 알람 리마인드 시간")
    val notificationRemindOption: NotificationRemindOption?,
    // todo : add remind noti level
    @Schema(description = "moit 시작 일자", example = "YYYY-MM-dd")
    val startDate: LocalDate,
    @Schema(description = "moit 종료 일자", example = "YYYY-MM-dd")
    val endDate: LocalDate,
) {
    companion object {
        fun of(moit: Moit, masterId: Long) = MoitDetailsResponse(
            moitId = moit.id,
            name = moit.name,
            masterId = masterId,
            description = moit.description,
            imageUrl = moit.imageUrl,
            scheduleDayOfWeeks = moit.scheduleDayOfWeeks,
            scheduleRepeatCycle = moit.scheduleRepeatCycle,
            scheduleStartTime = moit.scheduleStartTime,
            scheduleEndTime = moit.scheduleEndTime,
            fineLateTime = moit.fineLateTime,
            fineLateAmount = moit.fineLateAmount,
            fineAbsenceAmount = moit.fineAbsenceAmount,
            fineAbsenceTime = moit.fineAbsenceTime,
            notificationIsRemindActive = moit.notificationIsRemindActive,
            notificationRemindOption = moit.notificationRemindOption,
            startDate = moit.scheduleStartDate,
            endDate = moit.scheduleEndDate
        )
    }
}

@Schema(description = "Moit List Response")
data class MyMoitListResponse(
    @Schema(description = "Moit List")
    val moits: List<MyMoitResponseForListView>
)

@Schema(description = "Moit 간단 정보 - Moit List 조회에서 사용")
class MyMoitResponseForListView(
    @Schema(description = "Moit id")
    val id: Long,
    @Schema(description = "Moit 이름")
    val name: String,
    @Schema(description = "Moit 이미지 주소")
    val profileUrl: String? = null,
    @Schema(description = "Moit 종료 여부")
    val isEnd: Boolean = false,
    @Schema(description = "Moit Study 반복 주기 (반복X, 주, 격주, 3주, 4주)")
    val repeatCycle: ScheduleRepeatCycle,
    @Schema(description = "Moit Study 요일")
    val dayOfWeeks: Set<DayOfWeek>,
    @Schema(description = "Moit Study 시작시간")
    val startTime: LocalTime,
    @Schema(description = "Moit Study 종료시간")
    val endTime: LocalTime,
    @Schema(description = "제일 가까운 Moit Study D-day, 모든 Study 가 종료됐다면 null")
    val dday: Int?,
) {
    companion object {
        fun of(moit: Moit, dday: Int?) = MyMoitResponseForListView(
            id = moit.id,
            name = moit.name,
            profileUrl = moit.imageUrl,
            isEnd = moit.isEnd,
            repeatCycle = moit.scheduleRepeatCycle,
            dayOfWeeks = moit.scheduleDayOfWeeks,
            startTime = moit.scheduleStartTime,
            endTime = moit.scheduleEndTime,
            dday = dday,
        )
    }
}

@Schema(description = "Moit 스터디 조회 응답")
data class MoitStudyListResponse(
    @Schema(description = "Moit 내 모든 스터디 리스트")
    val studies: List<MoitStudyResponse>
) {
    companion object {
        fun sample(): MoitStudyListResponse = MoitStudyListResponse(
            studies = listOf(
                MoitStudyResponse.sample(),
                MoitStudyResponse.sample().copy(studyId = 2L)
            )
        )
    }
}

@Schema(description = "출결 정보를 담은 Moit 스터디 간단 정보")
data class MoitStudyResponse(
    @Schema(description = "출결 정보를 담은 스터디 id")
    val studyId: Long,
    @Schema(description = "스터디 순서")
    val order: Int,
    @Schema(description = "스터디 날짜")
    val date: LocalDate,
    @Schema(description = "스터디에 포함된 출결 리스트")
    val attendances: List<MoitStudyAttendanceResponse>
) {
    companion object {
        fun sample(): MoitStudyResponse = MoitStudyResponse(
            studyId = 1L,
            order = 0,
            date = LocalDate.of(2023, 6, 15),
            attendances = listOf(
                MoitStudyAttendanceResponse.sample().copy(status = AttendanceStatus.ABSENCE),
                MoitStudyAttendanceResponse.sample(),
                MoitStudyAttendanceResponse.sample().copy(
                    status = AttendanceStatus.LATE,
                    attendanceAt = LocalDateTime.of(2023, 6, 15, 17, 30)
                )
            )
        )
    }
}

@Schema(description = "Moit 스터디 출결 정보 응답")
data class MoitStudyAttendanceResponse(
    @Schema(description = "출석 유저 아이디")
    val userId: Long,
    @Schema(description = "출석 유저 닉네임")
    val nickname: String,
    @Schema(description = "출석 유저 프로필 이미지")
    val profileImage: Int,
    @Schema(description = "출석 상태")
    val status: AttendanceStatus,
    @Schema(description = "출석 시간")
    val attendanceAt: LocalDateTime? = null,
) {
    companion object {
        fun sample(): MoitStudyAttendanceResponse = MoitStudyAttendanceResponse(
            userId = 1L,
            nickname = "전자군단",
            profileImage = 3,
            status = AttendanceStatus.ATTENDANCE,
            attendanceAt = LocalDateTime.now()
        )
    }
}

@Schema(description = "Moit 가입 코드 조회 응답")
data class MoitInvitationCodeResponse(
    @Schema(description = "moit 초대 코드")
    @field:NotBlank
    @Size(min = 6, max = 6)
    val invitationCode: String,
) {
    companion object {
        fun of(invitationCode: String) = MoitInvitationCodeResponse(invitationCode)
    }
}
