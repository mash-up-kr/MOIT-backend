package com.mashup.moit.infra.event.kafka

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.banner.BannerService
import com.mashup.moit.domain.banner.update.MoitUnapprovedFineExistBannerUpdateRequest
import com.mashup.moit.domain.banner.update.StudyAttendanceStartBannerUpdateRequest
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.notification.AttendanceStartNotificationEvent
import com.mashup.moit.domain.notification.NotificationService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.FineApproveEvent
import com.mashup.moit.infra.event.FineCreateEvent
import com.mashup.moit.infra.event.FineCreateEventBulk
import com.mashup.moit.infra.event.KafkaConsumerGroup
import com.mashup.moit.infra.event.KafkaEventTopic
import com.mashup.moit.infra.event.MoitCreateEvent
import com.mashup.moit.infra.event.MoitJoinEvent
import com.mashup.moit.infra.event.StudyAttendanceEvent
import com.mashup.moit.infra.event.StudyAttendanceEventBulk
import com.mashup.moit.infra.event.StudyAttendanceStartNotificationPushEvent
import com.mashup.moit.infra.event.StudyInitializeEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class KafkaConsumer(
    private val studyService: StudyService,
    private val attendanceService: AttendanceService,
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

    /**
     * 스터디 시작이 20분 이내로 남아 이미 Attendance Initialize Scheduler 가 가동된 상황에서 모잇에 사용자가 신규 가입하면,
     * 해당 사용자는 Attendance Initialize 대상에 포함되어 있지 않기 때문에 곧 시작하는 스터디에 출석이 불가하다.
     * 이를 막기 위해 모잇에 신규 가입했을 때 스터디 시작이 20분 이내로 남았고 이미 Attendance Initialize Scheduler 가 가동됐다면
     * 다시 해당 스터디의 출석을 다시 initialize 하여 출석이 가능하게 한다.
     */
    @KafkaListener(
        topics = [KafkaEventTopic.MOIT_JOIN],
        groupId = KafkaConsumerGroup.MOIT_JOIN_ATTENDANCE_INITIALIZE,
    )
    fun consumeMoitJoinEvent(event: MoitJoinEvent) {
        log.debug("consumeMoitJoinEvent called: {}", event)
        studyService.findUpcomingStudy(event.moitId)
            ?.takeIf { LocalDateTime.now().plusMinutes(20).isAfter(it.startAt) && it.isInitialized }
            ?.run {
                attendanceService.initializeAttendance(this.id)
                eventProducer.produce(StudyInitializeEvent(this.id))
            }
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
        topics = [KafkaEventTopic.STUDY_ATTENDANCE_START_NOTIFICATION],
        groupId = KafkaConsumerGroup.STUDY_ATTENDANCE_START_NOTIFICATION_CREATE,
    )
    fun consumeStudyAttendanceStartNotificationPushEvent(event: StudyAttendanceStartNotificationPushEvent) {
        log.debug("consumeStudyAttendanceStartNotificationPushEvent called: {}", event)
        notificationService.save(AttendanceStartNotificationEvent(event.studyIdWithMoitIds, event.flushAt))
    }
}
