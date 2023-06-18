package com.mashup.moit.domain.attendance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttendanceRepository : JpaRepository<AttendanceEntity, Long> {
    fun findAllByStudyIdOrderByAttendanceAtAsc(studyId: Long): List<AttendanceEntity>
}
