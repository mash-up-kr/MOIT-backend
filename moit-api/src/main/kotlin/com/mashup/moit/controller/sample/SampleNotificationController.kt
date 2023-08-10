package com.mashup.moit.controller.sample

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
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
import com.mashup.moit.infra.fcm.SampleNotificationRequest
import com.mashup.moit.infra.fcm.ScheduledStudyNotification
import com.mashup.moit.infra.fcm.StudyAttendanceStartNotification
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/sample-noti")
class SampleNotificationController(
    private val fcmNotificationService: FCMNotificationService,
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val userService: UserService,
    private val fineService: FineService,
    private val eventProducer: EventProducer
) {

    val logger: Logger = LoggerFactory.getLogger(SampleNotificationController::class.java)

    @PostMapping("/pushs/topics")
    fun notificationTopics(): MoitApiResponse<Unit> {
        val sampleNotificationRequest = SampleNotificationRequest.sample()
        fcmNotificationService.sendTopicSampleNotification(sampleNotificationRequest)
        return MoitApiResponse.success()
    }

    @GetMapping("/push/start-study/{moitId}")
    fun pushStartStudy(@PathVariable moitId: Long): MoitApiResponse<Unit> {
        val moit = moitService.getMoitById(moitId)
        val study = studyService.findUpcomingStudy(moitId)
            ?: throw MoitException.of(MoitExceptionType.NOT_EXIST, "예정된 스터디가 없습니다.")

        fcmNotificationService.pushStudyNotification(StudyAttendanceStartNotification.of(moit, study))

        eventProducer.produce(StudyAttendanceStartNotificationPushEvent(studyIdWithMoitIds = setOf(study.id to moit.id), flushAt = LocalDateTime.now()))
        logger.info(
            "Done start Study Push notification & noti event Produce for moitId-{}, studyId-{}, studyOrder-{} at {}",
            moitId, study.id, study.order + 1, LocalDateTime.now()
        )
        return MoitApiResponse.success()
    }

    @GetMapping("/push/scheduled-study/{moitId}")
    fun pushScheduledStudy(@PathVariable moitId: Long): MoitApiResponse<Unit> {
        val moit = moitService.getMoitById(moitId)
        val study = studyService.findUpcomingStudy(moitId)
            ?: throw MoitException.of(MoitExceptionType.NOT_EXIST, "예정된 스터디가 없습니다.")

        fcmNotificationService.pushStudyNotification(ScheduledStudyNotification.of(moit, study))

        eventProducer.produce(ScheduledStudyNotificationPushEvent(studyIdWithMoitIds = setOf(study.id to moit.id), flushAt = LocalDateTime.now()))

        logger.info(
            "Done scheduled Study Push notification & noti event Produce for moitId-{}, studyId-{}, studyOrder-{} at {}",
            moitId, study.id, study.order + 1, LocalDateTime.now()
        )

        return MoitApiResponse.success()
    }

    @GetMapping("/push/remind-fine")
    fun pushRemindFine(@RequestParam moitId: Long, @RequestParam fineId: Long): MoitApiResponse<Unit> {
        val moit = moitService.getMoitById(moitId)
        val fine = fineService.getFine(fineId)
        val study = studyService.findById(fine.studyId)
        val user = userService.findUserById(fine.userId)

        FineRemindNotification.of(user, moit, study)
            ?.let { notification ->
                fcmNotificationService.pushRemindFineNotification(notification)
                eventProducer.produce(RemindFineNotificationPushEvent(fineIds = setOf(fine.id), flushAt = LocalDateTime.now()))
            }

        logger.info("Done remind Fine Push notification & noti event Produce for fineId-{},moitId-{}, at {}", fineId, moitId, LocalDateTime.now())
        return MoitApiResponse.success()
    }
}
