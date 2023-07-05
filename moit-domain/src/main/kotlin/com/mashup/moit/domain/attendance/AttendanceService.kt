package com.mashup.moit.domain.attendance

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
) {
    fun findAttendancesByStudyId(studyId: Long): List<Attendance> {
        return attendanceRepository.findAllByStudyIdOrderByAttendanceAtAsc(studyId)
            .takeIf { it.isNotEmpty() }
            ?.map { it.toDomain() }
            ?: throw MoitException.of(MoitExceptionType.ATTENDANCE_NOT_STARTED)
    }

    fun existFirstAttendanceByStudyId(studyId: Long): Boolean {
        return attendanceRepository.existsByStudyIdAndStatus(studyId, AttendanceStatus.ATTENDANCE)
    }
}
