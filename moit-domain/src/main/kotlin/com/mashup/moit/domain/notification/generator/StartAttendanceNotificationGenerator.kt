package com.mashup.moit.domain.notification.generator

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.notification.AttendanceStartNotificationEvent
import com.mashup.moit.domain.notification.NotificationEntity
import com.mashup.moit.domain.notification.NotificationEvent
import com.mashup.moit.domain.notification.NotificationType
import com.mashup.moit.domain.notification.RemindFineNotificationEvent
import com.mashup.moit.domain.notification.UrlSchemeParameter
import com.mashup.moit.domain.notification.UrlSchemeProperties
import com.mashup.moit.domain.notification.UrlSchemeUtils
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.domain.usermoit.UserMoitService
import org.springframework.stereotype.Component

@Component
class StartAttendanceNotificationGenerator(
    private val userMoitService: UserMoitService,
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    companion object {
        fun titleTemplate() = "[출석 체크 시작] ✔️"
        fun bodyTemplate(moitName: String, studyOrder: Int): String {
            return "$moitName ${studyOrder + 1}회차 스터디가 시작되었어요"
        }

        fun urlScheme(urlScheme: String, moitId: Long) = UrlSchemeUtils.generate(urlScheme, UrlSchemeParameter("moitId", moitId.toString()))
    }

    override fun support(event: NotificationEvent): Boolean {
        return event is AttendanceStartNotificationEvent
    }

    override fun generate(event: NotificationEvent): List<NotificationEntity> {
        if (event !is AttendanceStartNotificationEvent) {
            throw MoitException.of(MoitExceptionType.INVALID_NOTIFICATION_TYPE)
        }

        return event.studyIdWithMoitIds
            .flatMap { (studyId, moitId) ->
                val userIds = userMoitService.findUsersByMoitId(moitId).map { it.userId }
                val moitName = moitService.getMoitById(moitId).name
                val studyOrder = studyService.findById(studyId).order

                userIds.map { userId ->
                    NotificationEntity(
                        type = NotificationType.ATTENDANCE_START_NOTIFICATION,
                        userId = userId,
                        title = titleTemplate(),
                        body = bodyTemplate(moitName, studyOrder),
                        urlScheme = urlScheme(urlSchemeProperties.attendanceStart, moitId)
                    )
                }
            }
    }
}
