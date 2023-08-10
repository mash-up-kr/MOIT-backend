package com.mashup.moit.infra.event.kafka

import com.mashup.moit.domain.banner.BannerService
import com.mashup.moit.domain.banner.update.MoitUnapprovedFineExistBannerUpdateRequest
import com.mashup.moit.domain.banner.update.StudyAttendanceStartBannerUpdateRequest
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.notification.AttendanceStartNotificationEvent
import com.mashup.moit.domain.notification.NotificationService
import com.mashup.moit.domain.notification.RemindFineNotificationEvent
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.FineApproveEvent
import com.mashup.moit.infra.event.FineCreateEvent
import com.mashup.moit.infra.event.FineCreateEventBulk
import com.mashup.moit.infra.event.KafkaConsumerGroup
import com.mashup.moit.infra.event.KafkaEventTopic
import com.mashup.moit.infra.event.MoitCreateEvent
import com.mashup.moit.infra.event.NotificationPushEvent
import com.mashup.moit.infra.event.RemindFineNotificationPushEvent
import com.mashup.moit.infra.event.ScheduledStudyNotificationPushEvent
import com.mashup.moit.infra.event.StudyAttendanceEvent
import com.mashup.moit.infra.event.StudyAttendanceEventBulk
import com.mashup.moit.infra.event.StudyAttendanceStartNotificationPushEvent
import com.mashup.moit.infra.event.StudyInitializeEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
    private val studyService: StudyService,
    private val fineService: FineService,
    private val bannerService: BannerService,
    private val notificationService: NotificationService,
    private val eventProducer: EventProducer,
) {
    private val log: Logger = LoggerFactory.getLogger(KafkaConsumer::class.java)

    @KafkaListener(
        topics = [KafkaEventTopic.MOIT_CREATE],
        groupId = KafkaConsumerGroup.MOIT_CREATE_STUDY_CREATE,
    )
    fun consumeMoitCreateEvent(event: MoitCreateEvent) {
        log.debug("consumeMoitCreateEvent called: {}", event)
        studyService.createStudies(event.moitId)
    }

    @KafkaListener(
        topics = [KafkaEventTopic.STUDY_INITIALIZE],
        groupId = KafkaConsumerGroup.STUDY_INITIALIZE_BANNER_UPDATE,
    )
    fun consumeStudyInitializeEvent(event: StudyInitializeEvent) {
        log.debug("consumeStudyInitializeEvent called: {}", event)
        bannerService.update(StudyAttendanceStartBannerUpdateRequest(event.studyId))
    }

    @KafkaListener(
        topics = [KafkaEventTopic.STUDY_ATTENDANCE],
        groupId = KafkaConsumerGroup.STUDY_ATTENDANCE_FINE_CREATE,
    )
    fun consumeStudyAttendanceEvent(event: StudyAttendanceEvent) {
        log.debug("consumeStudyAttendanceEvent called: {}", event)
        fineService.create(event.attendanceId, event.moitId)?.also {
            eventProducer.produce(FineCreateEvent(fineId = it.id))
        }
    }

    @KafkaListener(
        topics = [KafkaEventTopic.STUDY_ATTENDANCE_BULK],
        groupId = KafkaConsumerGroup.STUDY_ATTENDANCE_FINE_CREATE_BULK,
    )
    fun consumeStudyAttendancesEvent(event: StudyAttendanceEventBulk) {
        log.debug("consumeStudyAttendancesEventBulk called: {}", event)
        val fineIds = mutableSetOf<Long>()
        event.attendanceIdWithMoitIds.forEach { (attendanceId, moitId) ->
            fineService.create(attendanceId, moitId)?.let { fine -> fineIds.add(fine.id) }
        }
        eventProducer.produce(FineCreateEventBulk(fineIds))
    }

    @KafkaListener(
        topics = [KafkaEventTopic.FINE_CREATE],
        groupId = KafkaConsumerGroup.FINE_CREATE_BANNER_UPDATE,
    )
    fun consumeFineCreateEvent(event: FineCreateEvent) {
        log.debug("consumeFineCreateEvent called: {}", event)
        bannerService.update(MoitUnapprovedFineExistBannerUpdateRequest(event.fineId))
    }

    @KafkaListener(
        topics = [KafkaEventTopic.FINE_CREATE_BULK],
        groupId = KafkaConsumerGroup.FINE_CREATE_BANNER_UPDATE_BULK,
    )
    fun consumeFineCreateEventBulk(event: FineCreateEventBulk) {
        log.debug("consumeFineCreateEventBulk called: {}", event)
        event.fineIds.forEach { fineId ->
            bannerService.update(MoitUnapprovedFineExistBannerUpdateRequest(fineId))
        }
    }

    @KafkaListener(
        topics = [KafkaEventTopic.FINE_APPROVE],
        groupId = KafkaConsumerGroup.FINE_APPROVE_BANNER_UPDATE,
    )
    fun consumeFineApproveEvent(event: FineApproveEvent) {
        log.debug("consumeFineApproveEvent called: {}", event)
        bannerService.update(MoitUnapprovedFineExistBannerUpdateRequest(event.fineId))
    }

    @KafkaListener(
        topics = [KafkaEventTopic.NOTIFICATION],
        groupId = KafkaConsumerGroup.NOTIFICATION_CREATE,
    )
    fun consumeNotificationPushEvent(event: NotificationPushEvent) {
        log.info("NotificationPushEvent called: {}", event)
        when (event) {
            is StudyAttendanceStartNotificationPushEvent -> notificationService.save(AttendanceStartNotificationEvent(event.studyIdWithMoitIds, event.flushAt))
            is RemindFineNotificationPushEvent -> notificationService.save(RemindFineNotificationEvent(event.fineIds, event.flushAt))
            is ScheduledStudyNotificationPushEvent -> notificationService.save(AttendanceStartNotificationEvent(event.studyIdWithMoitIds, event.flushAt))
        }
    }
}
