package com.mashup.moit.study.facade

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordRequest
import com.mashup.moit.study.controller.dto.StudyAttendanceKeywordResponse
import com.mashup.moit.study.controller.dto.StudyDetailsResponse
import com.mashup.moit.study.controller.dto.StudyFirstAttendanceResponse
import com.mashup.moit.study.controller.dto.StudyUserAttendanceStatusResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StudyFacade(
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val attendanceService: AttendanceService,
    private val userService: UserService,
    private val fineService: FineService,
) {
    fun getDetails(studyId: Long): StudyDetailsResponse {
        val study = studyService.findById(studyId)
        val moit = moitService.getMoitById(study.moitId)
        val firstAttendanceUser = study.firstAttendanceUserId?.let(userService::findByIdOrNull)
        return StudyDetailsResponse.of(moit, study, firstAttendanceUser)
    }

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

    @Transactional
    fun registerAttendanceKeyword(userId: Long, studyId: Long, request: StudyAttendanceKeywordRequest) {
        studyService.registerAttendanceKeyword(studyId, request.attendanceKeyword).also {
            val attendance = attendanceService.requestAttendance(userId, studyId)
            // TODO 비동기로 처리 고려
            fineService.create(attendance.id, it.moitId)
        }
    }

    @Transactional
    fun verifyAttendanceKeyword(userId: Long, studyId: Long, request: StudyAttendanceKeywordRequest) {
        studyService.verifyAttendanceKeyword(studyId, request.attendanceKeyword).also {
            val attendance = attendanceService.requestAttendance(userId, studyId)
            // TODO 비동기로 처리 고려
            fineService.create(attendance.id, it.moitId)
        }
    }

    @Transactional
    fun initializeAttendance(studyId: Long) {
        attendanceService.initializeAttendance(studyId)
        studyService.markAsInitialized(studyId)
    }

    fun checkFirstAttendance(studyId: Long): StudyFirstAttendanceResponse {
        return StudyFirstAttendanceResponse.of(attendanceService.existFirstAttendanceByStudyId(studyId))
    }
}
