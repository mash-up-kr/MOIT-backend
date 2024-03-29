package com.mashup.moit.domain.attendance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttendanceRepository : JpaRepository<AttendanceEntity, Long> {
    fun findByUserIdAndStudyId(userId: Long, studyId: Long): AttendanceEntity?
    fun findAllByStudyIdOrderByAttendanceAtAsc(studyId: Long): List<AttendanceEntity>
    fun findAllByStudyIdAndStatus(studyId: Long, status: AttendanceStatus): List<AttendanceEntity>
    fun existsByStudyIdAndStatusNot(studyId: Long, status: AttendanceStatus): Boolean
}
