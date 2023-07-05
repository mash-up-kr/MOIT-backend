package com.mashup.moit.domain.attendance

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.study.StudyEntity
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
        val userMoits = userMoitRepository.findAllByMoitId(study.moitId)

        userMoits.map {
            AttendanceEntity(
                studyId = studyId,
                userId = it.userId,
                status = AttendanceStatus.UNDECIDED,
                attendanceAt = null,
            )
        }.let { attendanceRepository.saveAll(it) }
    }

    @Transactional
    fun requestAttendance(userId: Long, studyId: Long) {
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
        attendance.apply {
            this.status = study.attendanceStatus(now)
            this.attendanceAt = now
        }
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

    private fun StudyEntity.attendanceStatus(dateTime: LocalDateTime): AttendanceStatus {
        return when {
            dateTime.isBefore(lateAt) -> AttendanceStatus.ATTENDANCE
            dateTime.isBefore(absenceAt) -> AttendanceStatus.LATE
            else -> AttendanceStatus.ABSENCE
        }
    }
}
