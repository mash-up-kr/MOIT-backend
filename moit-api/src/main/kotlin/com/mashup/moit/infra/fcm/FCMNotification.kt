package com.mashup.moit.infra.fcm

import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.study.Study

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

data class StudyAttendanceStartNotification(
    val moitId: Long,
    val title: String,
    val body: String,
) {
    companion object {
        private const val TITLE_TEMPLATE = "출석 체크가 시작되었어요!"

        // todo edit push message depends on notification level 
        fun of(moit: Moit, study: Study) = StudyAttendanceStartNotification(
            moitId = moit.id,
            title = TITLE_TEMPLATE,
            body = bodyTemplate(moit, study)
        )

        private fun bodyTemplate(moit: Moit, study: Study): String {
            return "${moit.name} ${study.order + 1}회차 스터디가 시작되었어요"
        }
    }
}
