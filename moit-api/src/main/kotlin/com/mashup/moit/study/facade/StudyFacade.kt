package com.mashup.moit.study.facade

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.study.controller.dto.StudyUserAttendanceStatusResponse
import org.springframework.stereotype.Component

@Component
class StudyFacade(
    private val attendanceService: AttendanceService,
    private val userService: UserService,
) {
    fun getUserAttendanceStatus(studyId: Long): List<StudyUserAttendanceStatusResponse> {
        val attendances = attendanceService.findAttendancesByStudyId(studyId)
        val usersById = userService.findUsersById(attendances.map { it.userId })
            .associateBy { it.id }

        return attendances.map { StudyUserAttendanceStatusResponse.of(it, usersById.getValue(it.userId)) }
    }
}
