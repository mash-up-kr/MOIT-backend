package com.mashup.moit.scheduler

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.StudyInitializeEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class AttendanceInitializeScheduler(
    private val studyService: StudyService,
    private val attendanceService: AttendanceService,
    private val eventProducer: EventProducer,
) {
    @Scheduled(cron = "0 */5 * * * *")
    @Async("asyncSchedulerExecutor")
    @Transactional
    fun initializeAttendance() {
        studyService.findUninitializedStudyStartAtBefore(LocalDateTime.now().plusMinutes(20)).forEach {
            attendanceService.initializeAttendance(it.id)
            studyService.markAsInitialized(it.id)
            eventProducer.produce(StudyInitializeEvent(it.id))
        }
    }
}
