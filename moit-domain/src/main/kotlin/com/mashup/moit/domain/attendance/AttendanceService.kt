package com.mashup.moit.domain.attendance

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.study.StudyRepository
import com.mashup.moit.domain.usermoit.UserMoitRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val studyRepository: StudyRepository,
    private val userMoitRepository: UserMoitRepository,
) {
    @Transactional
    fun initializeAttendance(studyId: Long) {
        val study = studyRepository.findById(studyId).orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
        val studyUserIds = userMoitRepository.findAllByMoitId(study.moitId).map { it.userId }

        val existedAttendanceUserIds = attendanceRepository.findAllByStudyIdOrderByAttendanceAtAsc(studyId).map { it.userId }

        studyUserIds.filter { it !in existedAttendanceUserIds }.map {
            AttendanceEntity(
                studyId = studyId,
                userId = it,
                status = AttendanceStatus.UNDECIDED,
                attendanceAt = null,
            )
        }.let { attendanceRepository.saveAll(it) }
    }

    @Transactional
    fun requestAttendance(userId: Long, studyId: Long): Attendance {
        val study = studyRepository.findById(studyId).orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
        val attendance = attendanceRepository.findByUserIdAndStudyId(userId, studyId)
            ?: throw MoitException.of(MoitExceptionType.ATTENDANCE_NOT_STARTED)
        if (attendance.status !== AttendanceStatus.UNDECIDED) {
            throw MoitException.of(
                MoitExceptionType.ALREADY_EXIST,
                "이미 출석하였습니다."
            )
        }

        val now = LocalDateTime.now()
        return attendance.apply {
            this.status = study.attendanceStatus(now)
            this.attendanceAt = now
        }.toDomain()
    }

    fun findAttendancesByStudyId(studyId: Long): List<Attendance> {
        return attendanceRepository.findAllByStudyIdOrderByAttendanceAtAsc(studyId)
            .takeIf { it.isNotEmpty() }
            ?.map { it.toDomain() }
            ?: throw MoitException.of(MoitExceptionType.ATTENDANCE_NOT_STARTED)
    }

    fun existFirstAttendanceByStudyId(studyId: Long): Boolean {
        return attendanceRepository.existsByStudyIdAndStatus(studyId, AttendanceStatus.ATTENDANCE)
    }

    @Transactional
    fun adjustUndecidedAttendancesByStudyId(studyId: Long, status: AttendanceStatus = AttendanceStatus.ABSENCE): List<Long> {
        val attendances = attendanceRepository.findAllByStudyIdAndStatus(studyId, AttendanceStatus.UNDECIDED)
        attendances.forEach { attendance -> attendance.status = status }
        return attendances.map { it.id }
    }
}
