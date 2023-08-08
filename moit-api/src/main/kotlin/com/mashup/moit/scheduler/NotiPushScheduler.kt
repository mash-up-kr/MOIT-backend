package com.mashup.moit.scheduler

import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.ScheduledStudyNotificationPushEvent
import com.mashup.moit.infra.event.StudyAttendanceStartNotificationPushEvent
import com.mashup.moit.infra.fcm.FCMNotificationService
import com.mashup.moit.infra.fcm.ScheduledStudyNotification
import com.mashup.moit.infra.fcm.StudyAttendanceStartNotification
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class NotiPushScheduler(
    private val studyService: StudyService,
    private val moitService: MoitService,
    private val fcmNotificationService: FCMNotificationService,
    private val eventProducer: EventProducer
) {
    val logger: Logger = LoggerFactory.getLogger(NotiPushScheduler::class.java)

    @Scheduled(cron = "0 */5 * * * *")
    @Async("pushSchedulerExecutor")
    fun pushStartAttendanceNotification() {
        val scheduleContext = LocalDateTime.now()
        val minContext = scheduleContext.minusMinutes(1)

        val startedStudy = studyService.findStudiesByStartTime(minContext, scheduleContext)
        logger.info("{} studies start! Start Push notification at {}.", startedStudy.size, scheduleContext)

        val studyMoitMap = startedStudy.associateWith { study ->
            moitService.getMoitById(study.moitId)
        }

        val studyIdWithMoitIds = studyMoitMap.entries.map {
            fcmNotificationService.pushStudyNotification(StudyAttendanceStartNotification.of(it.value, it.key))
            Pair(it.key.id, it.value.id)
        }.toSet()

        eventProducer.produce(StudyAttendanceStartNotificationPushEvent(studyIdWithMoitIds = studyIdWithMoitIds, flushAt = LocalDateTime.now()))

        logger.info("Done Push notification for {} studies, at {}.", startedStudy.size, LocalDateTime.now())
    }
    
    @Scheduled(cron = "0 */5 * * * *")
    @Async("pushSchedulerExecutor")
    fun pushScheduledStudy10AMRemindNotification() {
        val studies = studyService.findNotPushedStudies(LocalDateTime.now())

        val studyMoitMap = studies.associateWith { study ->
            moitService.getMoitById(study.moitId)
        }

        val studyIdWithMoitIds = studyMoitMap.entries.map {
            fcmNotificationService.pushStudyNotification(ScheduledStudyNotification.of(it.value, it.key))
            Pair(it.key.id, it.value.id)
        }.toSet()

        eventProducer.produce(
            ScheduledStudyNotificationPushEvent(studyIdWithMoitIds = studyIdWithMoitIds, flushAt = LocalDateTime.now())
        )

        studies.forEach { study -> studyService.markAsPushed(study.id) }
        logger.info("Done Push notification for {} scheduled studies, at {}.", studyMoitMap.size, LocalDateTime.now())
    }
}
