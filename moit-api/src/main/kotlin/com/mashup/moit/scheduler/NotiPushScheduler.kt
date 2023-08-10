package com.mashup.moit.scheduler

import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.RemindFineNotificationPushEvent
import com.mashup.moit.infra.event.ScheduledStudyNotificationPushEvent
import com.mashup.moit.infra.event.StudyAttendanceStartNotificationPushEvent
import com.mashup.moit.infra.fcm.FCMNotificationService
import com.mashup.moit.infra.fcm.FineRemindNotification
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
    private val userService: UserService,
    private val moitService: MoitService,
    private val fineService: FineService,
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
    fun pushScheduledStudyRemindNotification() {
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

    @Scheduled(cron = "0 30 10 * * *")
    @Async("pushSchedulerExecutor")
    fun pushRemindFineNotification() {
        val scheduleContext = org.joda.time.LocalDateTime.now()

        val unrequestedFines = fineService.getUnrequestedFines()
        logger.info("Remind {} fines! Start Push Notification at {}.", unrequestedFines.size, scheduleContext)

        val unrequestedFineUserMap = userService
            .findUsersById(
                unrequestedFines.map { fine -> fine.userId }.distinct()
            )
            .associateBy { user -> user.id }
        val unrequestedFineMoitMap = moitService
            .getMoitsByIds(
                unrequestedFines.map { fine -> fine.moitId }.toSet()
            )
            .associateBy { moit -> moit.id }
        val unrequestedFineStudyMap = studyService
            .findByStudyIds(
                unrequestedFines.map { fine -> fine.studyId }.distinct()
            )
            .associateBy { study -> study.id }

        for (fine in unrequestedFines) {
            val user = unrequestedFineUserMap[fine.userId] ?: continue
            val moit = unrequestedFineMoitMap[fine.moitId] ?: continue
            val study = unrequestedFineStudyMap[fine.studyId] ?: continue
            FineRemindNotification.of(
                user = user,
                moit = moit,
                study = study
            )?.let { notification -> fcmNotificationService.pushRemindFineNotification(notification) }
        }
        eventProducer.produce(
            RemindFineNotificationPushEvent(
                fineIds = unrequestedFines.map { fine -> fine.id }.toSet(),
                flushAt = LocalDateTime.now()
            )
        )
        logger.info("Done Push notification for {} fines, at {}", unrequestedFines.size, org.joda.time.LocalDateTime.now())
    }
}
