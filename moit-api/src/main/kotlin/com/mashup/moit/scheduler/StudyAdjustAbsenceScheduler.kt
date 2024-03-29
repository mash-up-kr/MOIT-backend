package com.mashup.moit.scheduler

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.StudyAttendanceEventBulk
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class StudyAdjustAbsenceScheduler(
    private val studyService: StudyService,
    private val attendanceService: AttendanceService,
    private val eventProducer: EventProducer
) {
    val logger: Logger = LoggerFactory.getLogger(StudyAdjustAbsenceScheduler::class.java)

    @Scheduled(cron = "0 */5 * * * *")
    @Async("asyncSchedulerExecutor")
    @Transactional
    fun adjustAbsenceStatusFromNow() {
        // 결석 확정을 지을 때 endAt을 현재 시간보다 15초 정도 유예기간을 줌. 5분마다 배치가 돌기 때문에 95% 시간 내에 끝난 스터디를 반환함
        val scheduleContext = LocalDateTime.now()
        val undecided = studyService
            .findUnfinalizedStudiesByEndAtBefore(scheduleContext.minusSeconds(DECIDE_ABSENCE_RANGE_SECONDS))
        logger.info("{} undecided studies start! Start adjusting absence status at {}.", undecided.size, scheduleContext)

        val attendanceIdWithMoitIds = mutableListOf<Pair<Long, Long>>()
        undecided.forEach { study ->
            attendanceIdWithMoitIds.addAll(
                attendanceService.adjustUndecidedAttendancesByStudyId(study.id)
                    .map { attendanceId -> attendanceId to study.moitId }
            )
        }
        eventProducer.produce(StudyAttendanceEventBulk(attendanceIdWithMoitIds))

        logger.info("Done adjusting absence status for {} studies, at {}", undecided.size, LocalDateTime.now())
    }

    companion object {
        private const val DECIDE_ABSENCE_RANGE_SECONDS = 15L
    }
}
