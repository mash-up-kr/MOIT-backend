package com.mashup.moit.domain.notification

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.domain.usermoit.UserMoitService
import org.springframework.stereotype.Component

interface NotificationGenerator {
    fun support(event: NotificationEvent): Boolean
    fun generate(event: NotificationEvent): List<NotificationEntity>
}


@Component
class StartAttendancePushNotificationGenerator(
    private val userMoitService: UserMoitService,
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    companion object {
        const val TITLE_TEMPLATE = "출석 체크가 시작되었어요!"
        fun bodyTemplate(moitName: String, studyOrder: Int): String {
            return "$moitName ${studyOrder + 1}회차 스터디가 시작되었어요"
        }
    }

    override fun support(event: NotificationEvent): Boolean {
        return event is AttendanceStartNotificationEvent
    }

    override fun generate(event: NotificationEvent): List<NotificationEntity> {
        if (event !is AttendanceStartNotificationEvent) {
            throw MoitException.of(MoitExceptionType.SYSTEM_FAIL)
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
                        title = TITLE_TEMPLATE,
                        body = bodyTemplate(moitName, studyOrder),
                        urlScheme = UrlSchemeUtils.generate(
                            urlSchemeProperties.attendanceStart,
                            UrlSchemeParameter("moitId", moitId.toString())
                        )
                    )
                }
            }
    }
}

@Component
class RemindFinePushNotificationGenerator(
    private val userService: UserService,
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val fineService: FineService,
    private val urlSchemeProperties: UrlSchemeProperties,
) : NotificationGenerator {
    companion object {
        const val TITLE_TEMPLATE = "밀린 벌금이 있어요!"
        fun bodyTemplate(userName: String, moitName: String, studyOrder: Int): String {
            return "$moitName ${userName}님, 아직 ${studyOrder + 1}회차 스터디의 벌금을 납부하지 않았습니다.\n얼른 납부하고 인증받으세요!"
        }
    }

    override fun support(event: NotificationEvent): Boolean {
        return event is RemindFineNotificationEvent
    }

    override fun generate(event: NotificationEvent): List<NotificationEntity> {
        if (event !is RemindFineNotificationEvent) {
            throw MoitException.of(MoitExceptionType.SYSTEM_FAIL)
        }

        return event.fineIds
            .map { fineId ->
                val fine = fineService.getFine(fineId)
                val user = userService.findUserById(fine.userId)
                val (userId, userName) = user.id to user.nickname
                val moit = moitService.getMoitById(fine.moitId)
                val (moitId, moitName) = moit.id to moit.name
                val studyOrder = studyService.findById(fine.studyId).order

                NotificationEntity(
                    type = NotificationType.CHECK_FINE_NOTIFICATION,
                    userId = userId,
                    title = TITLE_TEMPLATE,
                    body = bodyTemplate(userName, moitName, studyOrder),
                    urlScheme = UrlSchemeUtils.generate(
                        urlSchemeProperties.checkFine,
                        UrlSchemeParameter("moitId", moitId.toString()),
                        UrlSchemeParameter("fineId", fineId.toString())
                    )
                )
            }
    }
}
