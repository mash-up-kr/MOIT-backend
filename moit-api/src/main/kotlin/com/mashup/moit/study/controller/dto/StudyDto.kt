package com.mashup.moit.study.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "study 출석 키워드 조회 응답")
data class StudyAttendanceKeywordResponse(
    @Schema(description = "Study 출석 키워드")
    val attendanceKeyword: String,
) {
    companion object {
        fun sample() = StudyAttendanceKeywordResponse("벌금환영")
    }
}
