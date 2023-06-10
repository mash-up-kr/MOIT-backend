package com.mashup.moit.controller.dto


import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import io.swagger.v3.oas.annotations.media.Schema
import java.time.DayOfWeek

@Schema(description = "Moit List Response")
class MoitListResponse(
    @Schema(description = "Moit List")
    val moits: List<MoitResponseForListView>
)

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
