package com.mashup.moit.study.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Study 출석 키워드 조회 응답")
data class StudyAttendanceKeywordResponse(
    @Schema(description = "Study 출석 키워드")
    val attendanceKeyword: String,
) {
    companion object {
        fun sample() = StudyAttendanceKeywordResponse("벌금환영")
    }
}

@Schema(description = "Study 첫 출석자 존재 유무 응답")
data class StudyFirstAttendanceResponse(
    @Schema(description = "Study 첫 출석자 존재 유무")
    val isFirstAttendance: Boolean,
) {
    companion object {
        fun sample() = StudyFirstAttendanceResponse(true)
    }
}

