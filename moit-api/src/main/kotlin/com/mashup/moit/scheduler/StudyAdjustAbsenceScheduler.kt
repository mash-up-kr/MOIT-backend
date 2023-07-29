package com.mashup.moit.scheduler

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.study.StudyService
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
    private val fineService: FineService
) {
    val logger: Logger = LoggerFactory.getLogger(StudyAdjustAbsenceScheduler::class.java)

    @Scheduled(cron = "0 */5 * * * *")
    @Async("asyncSchedulerExecutor")
    @Transactional
    fun adjustAbsenceStatusFromNow() {
        // 결석 확정을 지을 때 endAt을 현재 시간보다 15초 정도 유예기간을 줌. 5분마다 배치가 돌기 때문에 95% 시간 내에 끝난 스터디를 반환함
        val scheduleContext = LocalDateTime.now()
        val undecided = studyService
            .findUnfinalizedStudiesByEndAtBefore(LocalDateTime.now().minusSeconds(DECIDE_ABSENCE_RANGE_SECONDS))
            .map { it.id }
        logger.info("{} undecided studies start! Start adjusting absence status at {}.", undecided.size, scheduleContext)

        studyService.findByStudyIds(undecided)
            .groupBy { study -> study.moitId }
            .forEach { (moitId, studies) ->
                studies.forEach { study ->
                    attendanceService.adjustUndecidedAttendancesByStudyId(study.id)
                        .forEach { attendanceId -> fineService.create(attendanceId, moitId) }
                }
            }

        logger.info("Done adjusting absence status for {} studies, at {}", undecided.size, LocalDateTime.now())
    }

    companion object {
        private const val DECIDE_ABSENCE_RANGE_SECONDS = 15L
    }
}
