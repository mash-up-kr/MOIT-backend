package com.mashup.moit.study.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Study", description = "Study 관련 Api 입니다.")
@RequestMapping("/api/v1/study")
@RestController
class StudyController {
    @Operation(summary = "Study Keyword API", description = "study 키워드 조회")
    @GetMapping("/{studyId}/attendance/keyword")
    fun getAttendanceKeyword(@PathVariable studyId: Long): MoitApiResponse<StudyAttendanceKeywordResponse> {
        return MoitApiResponse.success(StudyAttendanceKeywordResponse.sample())
    }
}
