package com.mashup.moit.fine.controller.dto

import com.mashup.moit.domain.attendance.AttendanceStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull


@Schema(description = "벌금 송금 평가 RequestBody")
data class FineEvaluateRequest(
    @Schema(description = "벌금 송금 승인 여부")
    @field:NotNull
    val confirm: Boolean,
)

@Schema(description = "벌금 리스트 조회 Response")
data class FineListResponse(
    @Schema(description = "벌금 총액")
    val totalFineAmount: Long,
    @Schema(description = "벌금 미납 리스트")
    val fineNotYet: List<FineResponseForListView>,
    @Schema(description = "벌금 납부 리스트")
    val fineComplete: List<FineResponseForListView>,
) {
    companion object {
        fun sample() = FineListResponse(
            totalFineAmount = 100000000000,
            fineNotYet = listOf(
                FineResponseForListView(
                    id = 2L,
                    fineAmount = 50000000000,
                    userId = 1L,
                    userName = "박재민",
                    attendanceStatus = AttendanceStatus.ABSENCE,
                    studyOrder = 5,
                    isConfirmed = false
                )
            ),
            fineComplete = listOf(
                FineResponseForListView(
                    id = 1L,
                    fineAmount = 50000000000,
                    userId = 1L,
                    userName = "박재민",
                    attendanceStatus = AttendanceStatus.ABSENCE,
                    studyOrder = 5,
                    isConfirmed = true
                )
            )
        )
    }
}

@Schema(description = "Fine 정보 - Fine List 조회에 사용")
data class FineResponseForListView(
    @Schema(description = "Fine id")
    val id: Long,
    @Schema(description = "Fine 금액")
    val fineAmount: Long,
    @Schema(description = "Fine 대상 User id")
    val userId: Long,
    @Schema(description = "Fine 대상 User 이름")
    val userName: String,
    @Schema(description = "Fine 대상 출석 상태 (LATE, ABSENCE)")
    val attendanceStatus: AttendanceStatus,
    @Schema(description = "Fine 대상 스터디 회차")
    val studyOrder: Int,
    @Schema(description = "Fine 납부 인증 유무")
    val isConfirmed: Boolean,
)
