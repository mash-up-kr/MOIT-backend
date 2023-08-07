package com.mashup.moit.infra.fcm

import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.notification.generator.RemindFinePushNotificationGenerator
import com.mashup.moit.domain.notification.generator.StartAttendanceNotificationGenerator
import com.mashup.moit.domain.study.Study
import com.mashup.moit.domain.user.User

data class SampleNotificationRequest(
    val studyId: Long,
    val studyName: String,
    val remainMinutes: NotificationRemindOption,
) {
    companion object {
        fun sample() = SampleNotificationRequest(
            studyId = 1L,
            studyName = "전자군단",
            remainMinutes = NotificationRemindOption.BEFORE_10_MINUTE
        )
    }
}

abstract class StudyNotification(
    open val moitId: Long,
    open val title: String,
    open val body: String,
)

data class StudyAttendanceStartNotification(
    override val moitId: Long,
    override val title: String,
    override val body: String,
) : StudyNotification(moitId, title, body) {
    companion object {
        // todo edit push message depends on notification level 
        fun of(moit: Moit, study: Study) = StudyAttendanceStartNotification(
            moitId = moit.id,
            title = StartAttendanceNotificationGenerator.titleTemplate(),
            body = StartAttendanceNotificationGenerator.bodyTemplate(moit.name, study.order)
        )
    }
}

data class FineRemindNotification(
    val userFcmToken: String,
    val title: String,
    val body: String
) {
    companion object {
        fun of(
            user: User,
            moit: Moit,
            study: Study
        ): FineRemindNotification? = user.fcmToken?.let { token ->
            FineRemindNotification(
                userFcmToken = token,
                title = RemindFinePushNotificationGenerator.TITLE_TEMPLATE,
                body = RemindFinePushNotificationGenerator.bodyTemplate(user.nickname, moit.name, study.order)
            )
        }
    }
}

data class ScheduledStudyNotification(
    override val moitId: Long,
    override val title: String,
    override val body: String,
) : StudyNotification(moitId, title, body) {
    companion object {
        fun of(moit: Moit, study: Study) = ScheduledStudyNotification(
            moitId = moit.id,
            title = "NPE",
            body = "NPE"
        )
    }
}
