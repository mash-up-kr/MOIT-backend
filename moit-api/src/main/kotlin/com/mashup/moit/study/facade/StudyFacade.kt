package com.mashup.moit.study.facade

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.study.controller.dto.StudyUserAttendanceStatusResponse
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordRequest
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordResponse
import org.springframework.stereotype.Component

@Component
class StudyFacade(
    private val studyService: StudyService,
    private val attendanceService: AttendanceService,
    private val userService: UserService,
) {
    fun getUserAttendanceStatus(studyId: Long): List<StudyUserAttendanceStatusResponse> {
        val attendances = attendanceService.findAttendancesByStudyId(studyId)
        val usersById = userService.findUsersById(attendances.map { it.userId })
            .associateBy { it.id }

        return attendances.map { StudyUserAttendanceStatusResponse.of(it, usersById.getValue(it.userId)) }
    }
    
    fun getAttendanceKeyword(studyId: Long): StudyAttendanceKeywordResponse {
        return studyService.getAttendanceKeyword(studyId)
            .let { StudyAttendanceKeywordResponse.of(it) }
    }

    fun registerAttendanceKeyword(studyId: Long, request: StudyAttendanceKeywordRequest) {
        studyService.registerAttendanceKeyword(studyId, request.attendanceKeyword)
    }

    fun verifyAttendanceKeyword(studyId: Long, request: StudyAttendanceKeywordRequest) {
        studyService.verifyAttendanceKeyword(studyId, request.attendanceKeyword)
    }
}
