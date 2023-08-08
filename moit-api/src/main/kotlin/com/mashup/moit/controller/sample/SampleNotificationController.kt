package com.mashup.moit.controller.sample

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.fcm.FCMNotificationService
import com.mashup.moit.infra.fcm.SampleNotificationRequest
import com.mashup.moit.infra.fcm.ScheduledStudyNotification
import com.mashup.moit.infra.fcm.StudyAttendanceStartNotification
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sample-noti")
class SampleNotificationController(
    private val fcmNotificationService: FCMNotificationService,
    private val moitService: MoitService,
    private val studyService: StudyService
) {
    @PostMapping("/pushs/topics")
    fun notificationTopics(): MoitApiResponse<Unit> {
        val sampleNotificationRequest = SampleNotificationRequest.sample()
        fcmNotificationService.sendTopicSampleNotification(sampleNotificationRequest)
        return MoitApiResponse.success()
    }

    @GetMapping("/push/start-study/{moitId}")
    fun pushStartStudy(@PathVariable moitId: Long): MoitApiResponse<Unit> {
        val moit = moitService.getMoitById(moitId)
        val study = studyService.findById(moitId)

        fcmNotificationService.pushStudyNotification(StudyAttendanceStartNotification.of(moit, study))
        return MoitApiResponse.success()
    }

    @GetMapping("/push/scheduled-study/{moitId}")
    fun pushScheduledStudy(@PathVariable moitId: Long): MoitApiResponse<Unit> {
        val moit = moitService.getMoitById(moitId)
        val study = studyService.findById(moitId)

        fcmNotificationService.pushStudyNotification(ScheduledStudyNotification.of(moit, study))
        return MoitApiResponse.success()
    }
}
