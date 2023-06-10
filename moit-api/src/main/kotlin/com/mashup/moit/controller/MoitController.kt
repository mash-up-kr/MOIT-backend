package com.mashup.moit.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.common.formatLocalTime
import com.mashup.moit.controller.dto.MoitListResponse
import com.mashup.moit.controller.dto.MoitResponseForListView
import com.mashup.moit.domain.moit.ScheduleRepeatCycle
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek
import java.time.LocalTime

@RestController
@RequestMapping("/api/v1/moit")
class MoitController {

    @Operation(summary = "Moit Details API", description = "moit 상세 조회")
    @GetMapping
    fun moitList(): MoitApiResponse<MoitListResponse> {
        val now = LocalTime.now()
        val response = MoitListResponse(
            listOf(
                MoitResponseForListView(
                    1L,
                    "가나다라마바사",
                    null,
                    false,
                    ScheduleRepeatCycle.ONE_WEEK,
                    setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                    now.formatLocalTime(),
                    now.plusHours(2).formatLocalTime(),
                    10
                ),
                MoitResponseForListView(
                    2L,
                    "moit 😃",
                    null,
                    true,
                    ScheduleRepeatCycle.THREE_WEEK,
                    setOf(DayOfWeek.THURSDAY),
                    now.plusMinutes(30).formatLocalTime(),
                    now.plusHours(4).formatLocalTime(),
                    5
                ),
                MoitResponseForListView(
                    3L,
                    "mock moit",
                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2F%25EC%25BC%2580%25EB%25A1%259C%25EB%25A1%259CM&psig=AOvVaw2vGyIh3ZuUGB0jkUxEK25z&ust=1686475431606000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCNjp_NmwuP8CFQAAAAAdAAAAABAE",
                    false,
                    ScheduleRepeatCycle.ONE_WEEK,
                    setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                    now.minusHours(3).formatLocalTime(),
                    now.plusHours(2).formatLocalTime(),
                    0
                ),
            )
        )
        return MoitApiResponse.success(response)
    }

}
