package com.mashup.moit.moit.controller.dto


import com.mashup.moit.common.util.DateTimeUtils.responseFormatTime
import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import io.swagger.v3.oas.annotations.media.Schema
import java.time.DayOfWeek
import java.time.LocalTime

@Schema(description = "Moit List Response")
class MoitListResponse(
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
                        ScheduleRepeatCycle.THREE_WEEK,
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
