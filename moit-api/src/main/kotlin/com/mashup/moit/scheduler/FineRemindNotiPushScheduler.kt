package com.mashup.moit.scheduler

import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.RemindFineNotificationPushEvent
import com.mashup.moit.infra.fcm.FCMNotificationService
import com.mashup.moit.infra.fcm.FineRemindNotification
import org.joda.time.LocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FineRemindNotiPushScheduler(
    private val userService: UserService,
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val fineService: FineService,
    private val fcmNotificationService: FCMNotificationService,
    private val eventProducer: EventProducer
) {

    val logger: Logger = LoggerFactory.getLogger(FineRemindNotiPushScheduler::class.java)

    @Scheduled(cron = "0 30 10 * * *")
    @Async("pushSchedulerExecutor")
    fun pushRemindFineNotification() {
        val scheduleContext = LocalDateTime.now()

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
                flushAt = java.time.LocalDateTime.now())
        )
        logger.info("Done Push notification for {} fines, at {}", unrequestedFines.size, LocalDateTime.now())
    }
}
