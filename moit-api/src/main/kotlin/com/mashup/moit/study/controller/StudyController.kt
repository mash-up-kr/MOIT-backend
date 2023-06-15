package com.mashup.moit.study.controller

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordResponse
import com.mashup.moit.study.controller.dto.StudyFirstAttendanceResponse
import com.mashup.moit.study.facade.StudyFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Study", description = "Study 관련 Api 입니다.")
@RequestMapping("/api/v1/study")
@RestController
class StudyController(
    private val studyFacade: StudyFacade
) {

    @Operation(summary = "Study Keyword API", description = "study 키워드 조회")
    @GetMapping("/{studyId}/attendance/keyword")
    fun getAttendanceKeyword(@PathVariable studyId: Long): MoitApiResponse<StudyAttendanceKeywordResponse> {
        return MoitApiResponse.success(StudyAttendanceKeywordResponse.sample())
    }

    @Operation(summary = "Study 첫 출석자 존재 유무 확인 API", description = "Study에 출석한 사람이 있는지 유무 조회")
    @GetMapping("/{studyId}/attendance/is-first")
    fun isFirstAttendance(@PathVariable studyId: Long): MoitApiResponse<StudyFirstAttendanceResponse> {
        return MoitApiResponse.success(StudyFirstAttendanceResponse.sample())
    }

}
