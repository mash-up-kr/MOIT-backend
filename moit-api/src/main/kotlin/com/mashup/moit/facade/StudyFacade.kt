package com.mashup.moit.facade

import com.mashup.moit.controller.study.dto.StudyAttendanceKeywordRequest
import com.mashup.moit.controller.study.dto.StudyAttendanceKeywordResponse
import com.mashup.moit.controller.study.dto.StudyDetailsResponse
import com.mashup.moit.controller.study.dto.StudyFirstAttendanceResponse
import com.mashup.moit.controller.study.dto.StudyUserAttendanceStatusResponse
import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.StudyAttendanceEvent
import com.mashup.moit.infra.event.StudyInitializeEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class StudyFacade(
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val attendanceService: AttendanceService,
    private val fineService: FineService,
    private val userService: UserService,
    private val eventProducer: EventProducer
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
            eventProducer.produce(StudyAttendanceEvent(attendanceId = attendance.id, moitId = it.moitId))
        }
    }

    @Transactional
    fun verifyAttendanceKeyword(userId: Long, studyId: Long, request: StudyAttendanceKeywordRequest) {
        studyService.verifyAttendanceKeyword(studyId, request.attendanceKeyword).also {
            val attendance = attendanceService.requestAttendance(userId, studyId)
            eventProducer.produce(StudyAttendanceEvent(attendanceId = attendance.id, moitId = it.moitId))
        }
    }

    @Transactional
    fun initializeAttendance(studyId: Long) {
        attendanceService.initializeAttendance(studyId)
        studyService.markAsInitialized(studyId)
        eventProducer.produce(StudyInitializeEvent(studyId = studyId))
    }

    fun checkFirstAttendance(studyId: Long): StudyFirstAttendanceResponse {
        return StudyFirstAttendanceResponse.of(attendanceService.existFirstAttendanceByStudyId(studyId))
    }

    @Transactional
    fun adjustAbsenceStatus(studyIds: Set<Long>) {
        studyService.findByStudyIds(studyIds.toList()).forEach { study ->
            attendanceService.adjustUndecidedAttendancesByStudyId(study.id)
                .forEach { attendanceId -> fineService.create(attendanceId, study.moitId) }
        }
    }
}
