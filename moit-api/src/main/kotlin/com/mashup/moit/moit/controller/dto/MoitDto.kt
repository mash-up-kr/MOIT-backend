package com.mashup.moit.moit.controller.dto

import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.NotificationRemindLevel
import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import com.mashup.moit.domain.usermoit.UserMoit
import io.swagger.v3.oas.annotations.media.Schema
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Schema(description = "moit 가입 응답")
data class MoitJoinResponse(
    @Schema(description = "생성된 userMoitId")
    val id: Long,
    @Schema(description = "가입한 moitId")
    val moitId: Long,
    @Schema(description = "가입한 userId")
    val userId: Long,
    @Schema(description = "moit 내 권한")
    val role: String,
    @Schema(description = "moit 반복 요일")
    val scheduleDayOfWeeks: Set<DayOfWeek>,
    @Schema(description = "moit 반복 주기")
    val scheduleRepeatCycle: ScheduleRepeatCycle,
    @Schema(description = "moit 시작 시간")
    val scheduleStartTime: LocalTime,
    @Schema(description = "moit 종료 시간")
    val scheduleEndTime: LocalTime,
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
    @Schema(description = "moit 알람 리마인드 매운맛")
    val notificationRemindLevel: NotificationRemindLevel?,
    @Schema(description = "moit 시작 일자")
    val startDate: LocalDate,
    @Schema(description = "moit 종료 일자")
    val endDate: LocalDate,
) {
    companion object {
        fun of(moit: Moit, userMoit: UserMoit) = MoitJoinResponse(
            id = userMoit.id,
            moitId = userMoit.moitId,
            userId = userMoit.userId,
            role = userMoit.role,
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
            notificationRemindLevel = moit.notificationRemindLevel,
            startDate = moit.startDate,
            endDate = moit.endDate
        )
    }
}
