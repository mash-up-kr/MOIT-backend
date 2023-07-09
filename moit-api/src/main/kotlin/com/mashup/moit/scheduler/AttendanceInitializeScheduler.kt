package com.mashup.moit.scheduler

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.study.StudyService
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class AttendanceInitializeScheduler(
    private val studyService: StudyService,
    private val attendanceService: AttendanceService,
) {
    @Scheduled(cron = "0 */5 * * * *")
    @Async("asyncSchedulerExecutor")
    @Transactional
    fun initializeAttendance() {
        studyService.findUnInitializedStudyStartAtBefore(LocalDateTime.now().plusMinutes(20)).forEach {
            attendanceService.initializeAttendance(it.id)
            studyService.markAsInitialized(it.id)
        }
    }
}
