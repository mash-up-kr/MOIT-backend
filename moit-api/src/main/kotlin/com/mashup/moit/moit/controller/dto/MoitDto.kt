package com.mashup.moit.moit.controller.dto

import com.mashup.moit.common.util.DateTimeUtils
import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDate

@Schema(description = "moit 가입 RequestBody")
data class MoitJoinRequest(
    @Schema(description = "moit 초대 코드")
    @field:NotBlank
    @Size(min = 8, max = 8)
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
            scheduleStartTime = DateTimeUtils.formatLocalTime(moit.scheduleStartTime),
            scheduleEndTime = DateTimeUtils.formatLocalTime(moit.scheduleEndTime),
            fineLateTime = moit.fineLateTime,
            fineLateAmount = moit.fineLateAmount,
            fineAbsenceAmount = moit.fineAbsenceAmount,
            fineAbsenceTime = moit.fineAbsenceTime,
            notificationIsRemindActive = moit.notificationIsRemindActive,
            notificationRemindOption = moit.notificationRemindOption,
            startDate = moit.startDate,
            endDate = moit.endDate
        )

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
            notificationRemindOption = NotificationRemindOption.BEFORE_TEN_MINUTE,
            startDate = LocalDate.of(2023, 5, 30),
            endDate = LocalDate.of(2023, 8, 19)
        )
    }
}
