package com.mashup.moit.scheduler

import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.StudyAttendanceStartNotificationPushEvent
import com.mashup.moit.infra.fcm.FCMNotificationService
import com.mashup.moit.infra.fcm.StudyAttendanceStartNotification
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AttendanceStartNotiPushScheduler(
    private val studyService: StudyService,
    private val moitService: MoitService,
    private val fcmNotificationService: FCMNotificationService,
    private val eventProducer: EventProducer
) {
    val logger: Logger = LoggerFactory.getLogger(AttendanceStartNotiPushScheduler::class.java)

    @Scheduled(cron = "0 */5 * * * *")
    @Async("pushSchedulerExecutor")
    fun pushStartAttendanceNotification() {
        // schedule 특성상 5분 단위로 시점 = a1 일 떄 , 해당 변수(B)는 a1 보다 크다. 
        // 5분 단위의 스터디 시작 시간을 찾기 위해서 : a0 < B - 1m < a1 < B 이용
        val scheduleContext = LocalDateTime.now()
        val minContext = scheduleContext.minusMinutes(1)

        val startedStudy = studyService.findStudiesByStartTime(minContext, scheduleContext)
        logger.info("{} studies start! Start Push notification at {}.", startedStudy.size, scheduleContext)

        val studyMoitMap = startedStudy.associateWith { study ->
            moitService.getMoitById(study.moitId)
        }

        studyMoitMap.entries.forEach {
            fcmNotificationService.pushStartStudyNotification(StudyAttendanceStartNotification.of(it.value, it.key))
        }

        val studyIds = startedStudy.map { it.id }.toSet()
        eventProducer.produce(StudyAttendanceStartNotificationPushEvent(studyIds))

        logger.info("Done Push notification for {} studies, at {}.", startedStudy.size, LocalDateTime.now())
    }
}
