package com.mashup.moit.scheduler

import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.fcm.FCMNotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AttendanceStartNotiPushScheduler(
    private val studyService: StudyService,
    private val moitService: MoitService,
    private val fcmNotificationService: FCMNotificationService
) {
    val logger: Logger = LoggerFactory.getLogger(AttendanceStartNotiPushScheduler::class.java)

    fun pushStartAttendanceNotification() {
        // schedule 특성상 5분 단위로 시점 = a1 일 떄 , 해당 변수(B)는 a1 보다 크다. 
        // 5분 단위의 스터디 시작 시간을 찾기 위해서 : a0 < B - 1m < a1 < B 이용
        val scheduleContext = LocalDateTime.now()
        val minContext = scheduleContext.minusMinutes(1)

        val startedStudy = studyService.findStudiesByStartTime(minContext, scheduleContext)
        logger.info("{} studies start! Start Push notification.", startedStudy.size)

        val moitList = startedStudy.map { study ->
            moitService.getMoitById(study.moitId)
        }
    }
}
