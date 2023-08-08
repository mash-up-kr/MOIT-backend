package com.mashup.moit.domain.notification.generator

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.notification.NotificationEntity
import com.mashup.moit.domain.notification.NotificationEvent
import com.mashup.moit.domain.notification.NotificationType
import com.mashup.moit.domain.notification.ScheduledStudyNotificationEvent
import com.mashup.moit.domain.notification.UrlSchemeParameter
import com.mashup.moit.domain.notification.UrlSchemeProperties
import com.mashup.moit.domain.notification.UrlSchemeUtils
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.usermoit.UserMoitService
import org.springframework.stereotype.Component
import java.time.LocalTime

@Component
class ScheduledStudyNotificationGenerator(
    private val userMoitService: UserMoitService,
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    companion object {
        fun titleTemplate(moitName: String, startAt: LocalTime): String {
            return if (startAt.minute == 0) {
                "[예정 스터디] 오늘은 $moitName 가 ${startAt.hour} 시에 예정되어 있어요!"
            } else {
                "[예정 스터디] 오늘은 $moitName 가 ${startAt.hour}시 ${startAt.minute}분에 예정되어 있어요!"
            }
        }

        fun bodyTemplate(studyOrder: Int) = "좀 있다 ${studyOrder + 1}회차 스터디 출석 체크 때 만나요~👋"
        fun urlScheme(urlScheme: String, moitId: Long) = UrlSchemeUtils.generate(urlScheme, UrlSchemeParameter("moitId", moitId.toString()))
    }

    override fun support(event: NotificationEvent): Boolean {
        return event is ScheduledStudyNotificationEvent
    }

    override fun generate(event: NotificationEvent): List<NotificationEntity> {
        if (event !is ScheduledStudyNotificationEvent) {
            throw MoitException.of(MoitExceptionType.INVALID_NOTIFICATION_TYPE)
        }

        return event.studyIdWithMoitIds
            .flatMap { (studyId, moitId) ->
                val userIds = userMoitService.findUsersByMoitId(moitId).map { it.userId }
                val moit = moitService.getMoitById(moitId)
                val studyOrder = studyService.findById(studyId).order

                userIds.map { userId ->
                    NotificationEntity(
                        type = NotificationType.STUDY_SCHEDULED_NOTIFICATION,
                        userId = userId,
                        title = titleTemplate(moit.name, moit.scheduleStartTime),
                        body = bodyTemplate(studyOrder),
                        urlScheme = urlScheme(urlSchemeProperties.studyScheduled, moitId)
                    )
                }
            }
    }
}

