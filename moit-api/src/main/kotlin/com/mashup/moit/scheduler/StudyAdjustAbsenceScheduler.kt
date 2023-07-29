package com.mashup.moit.scheduler

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.study.StudyService
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

    @Scheduled(cron = "0 */5 * * * *")
    @Async("asyncSchedulerExecutor")
    @Transactional
    fun adjustAbsenceStatusFromNow() {
        // 결석 확정을 지을 때 endAt을 현재 시간보다 15초 정도 유예기간을 줌. 5분마다 배치가 돌기 때문에 95% 시간 내에 끝난 스터디를 반환함
        val undecided = studyService.findUnfinalizedStudiesByEndAt(LocalDateTime.now().minusSeconds(DECIDE_ABSENCE_RANGE))
            .map { it.id }
        studyService.findByStudyIds(undecided)
            .groupBy { study -> study.moitId }
            .forEach { (moitId, studies) ->
                studies.forEach { study ->
                    attendanceService.adjustUndecidedAttendancesByStudyId(study.id)
                        .forEach { attendanceId -> fineService.create(attendanceId, moitId) }
                }
            }
    }

    companion object {
        private const val DECIDE_ABSENCE_RANGE = 15L
    }
}
