package com.mashup.moit.moit.controller.dto

import com.mashup.moit.common.util.DateTimeUtils.responseFormatTime
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
    val lateAmount: Int,
    @field:NotNull
    @field:PositiveOrZero
    val absenceTime: Int,
    @field:NotNull
    @field:PositiveOrZero
    val absenceAmount: Int,
    @field:NotNull
    val isRemindActive: Boolean,
    val remindOption: NotificationRemindOption?,
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
    val scheduleStartTime: String,
    @Schema(description = "moit 종료 시간", example = "HH:mm")
    val scheduleEndTime: String,
    @Schema(description = "moit 지각 시간")
    val fineLateTime: Int,
    @Schema(description = "moit 지각 벌금")
    val fineLateAmount: Int,
    @Schema(description = "moit 결석 시간")
    val fineAbsenceTime: Int,
    @Schema(description = "moit 결석 벌금")
    val fineAbsenceAmount: Int,
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
            scheduleStartTime = moit.scheduleStartTime.responseFormatTime(),
            scheduleEndTime = moit.scheduleEndTime.responseFormatTime(),
            fineLateTime = moit.fineLateTime,
            fineLateAmount = moit.fineLateAmount,
            fineAbsenceAmount = moit.fineAbsenceAmount,
            fineAbsenceTime = moit.fineAbsenceTime,
            notificationIsRemindActive = moit.notificationIsRemindActive,
            notificationRemindOption = moit.notificationRemindOption,
            startDate = moit.scheduleStartDate,
            endDate = moit.scheduleEndDate
        )

        // TODO: mock data. remove when logic is configured.
        fun sample() = MoitDetailsResponse(
            moitId = 1,
            name = "전자군단 1등 시상 스터디",
            masterId = 1,
            description = "해커톤에서 1등 시상한다",
            imageUrl = "ABCDEFG",
            scheduleDayOfWeeks = setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
            scheduleRepeatCycle = ScheduleRepeatCycle.ONE_WEEK,
            scheduleStartTime = "20:00",
            scheduleEndTime = "22:00",
            fineLateTime = 10,
            fineLateAmount = 3000,
            fineAbsenceTime = 30,
            fineAbsenceAmount = 10000,
            notificationIsRemindActive = true,
            notificationRemindOption = NotificationRemindOption.BEFORE_10_MINUTE,
            startDate = LocalDate.of(2023, 5, 30),
            endDate = LocalDate.of(2023, 8, 19)
        )
    }
}

@Schema(description = "Moit List Response")
data class MoitListResponse(
    @Schema(description = "Moit List")
    val moits: List<MoitResponseForListView>
) {
    companion object {
        // TODO: mock data. remove when logic is configured.
        fun sample(): MoitListResponse {
            val now = LocalTime.now()
            return MoitListResponse(
                listOf(
                    MoitResponseForListView(
                        1L,
                        "가나다라마바사",
                        null,
                        false,
                        ScheduleRepeatCycle.ONE_WEEK,
                        setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                        now.responseFormatTime(),
                        now.plusHours(2).responseFormatTime(),
                        10
                    ),
                    MoitResponseForListView(
                        2L,
                        "moit 😃",
                        null,
                        true,
                        ScheduleRepeatCycle.TWO_WEEK,
                        setOf(DayOfWeek.THURSDAY),
                        now.plusMinutes(30).responseFormatTime(),
                        now.plusHours(4).responseFormatTime(),
                        5
                    ),
                    MoitResponseForListView(
                        3L,
                        "mock moit",
                        "https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2F%25EC%25BC%2580%25EB%25A1%259C%25EB%25A1%259CM&psig=AOvVaw2vGyIh3ZuUGB0jkUxEK25z&ust=1686475431606000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCNjp_NmwuP8CFQAAAAAdAAAAABAE",
                        false,
                        ScheduleRepeatCycle.ONE_WEEK,
                        setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                        now.minusHours(3).responseFormatTime(),
                        now.plusHours(2).responseFormatTime(),
                        0
                    ),
                )
            )
        }
    }
}

@Schema(description = "Moit 간단 정보 - Moit List 조회에서 사용")
class MoitResponseForListView(
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
    val startTime: String,
    @Schema(description = "Moit Study 종료시간")
    val endTime: String,
    @Schema(description = "제일 가까운 Moit Study D-day")
    val dday: Int
)
