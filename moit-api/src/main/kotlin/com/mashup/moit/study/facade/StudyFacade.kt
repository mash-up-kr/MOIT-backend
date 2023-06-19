package com.mashup.moit.study.facade

import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordRequest
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordResponse
import org.springframework.stereotype.Component

@Component
class StudyFacade(
    private val studyService: StudyService
) {
    fun getAttendanceKeyword(studyId: Long): StudyAttendanceKeywordResponse {
        return studyService.getAttendanceKeyword(studyId)
            .let { StudyAttendanceKeywordResponse.of(it) }
    }

    fun registerAttendanceKeyword(studyId: Long, request: StudyAttendanceKeywordRequest) {
        studyService.registerAttendanceKeyword(studyId, request.attendanceKeyword)
    }
}
