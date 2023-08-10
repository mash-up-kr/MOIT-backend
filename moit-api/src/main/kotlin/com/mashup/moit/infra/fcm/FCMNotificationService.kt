package com.mashup.moit.infra.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.notification.UrlSchemeProperties
import com.mashup.moit.domain.notification.generator.ScheduledStudyNotificationGenerator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FCMNotificationService(
    private val firebaseMessaging: FirebaseMessaging,
    private val urlSchemeProperties: UrlSchemeProperties
) {
    private val logger = LoggerFactory.getLogger(FCMNotificationService::class.java)

    fun pushStudyNotification(studyNotification: StudyNotification) {
        val topic = getMoitTopic(studyNotification.moitId)

        runCatching {
            val notification = Notification.builder()
                .setTitle(studyNotification.title)
                .setBody(studyNotification.body)
                .build()

            val urlScheme = when (studyNotification) {
                is ScheduledStudyNotification -> urlSchemeProperties.studyScheduled
                is StudyAttendanceStartNotification -> urlSchemeProperties.attendanceStart
                else -> throw MoitException.of(MoitExceptionType.INVALID_NOTIFICATION_TYPE)
            }
            val data = mapOf("urlScheme" to ScheduledStudyNotificationGenerator.urlScheme(urlScheme, studyNotification.moitId))

            val message = Message.builder()
                .setTopic(topic)
                .putAllData(data)
                .setNotification(notification)
                .build()

            firebaseMessaging.send(message)
        }.onSuccess { res ->
            logger.info("success to send notification : {}", res)
        }.onFailure { e ->
            logger.error("Fail to send scheduled Study Noti. topic-id : [{}], error: [{}]", topic, e.printStackTrace())
        }
    }

    fun pushRemindFineNotification(fineNotification: FineRemindNotification) {
        val userFcmToken = fineNotification.userFcmToken
        runCatching {
            val notification = Notification.builder()
                .setTitle(fineNotification.title)
                .setBody(fineNotification.body)
                .build()

            val message = Message.builder()
                .setToken(userFcmToken)
                .setNotification(notification)
                .build()

            firebaseMessaging.send(message)
        }.onSuccess { response ->
            logger.info("success to send notification : {}", response)
        }.onFailure { e ->
            logger.error("Fail to send Remind Fine Noti. userFcmToken : [{}], error: [{}]", userFcmToken, e.toString())
        }
    }

    fun sendTopicSampleNotification(request: SampleNotificationRequest) {
        val title = STUDY_REMINDER_NOTIFICATION
        val body = when (request.remainMinutes) {
            NotificationRemindOption.STUDY_DAY_10_AM -> "오늘은 ${request.studyName} 스터디가 있는 날이에요"
            else -> "${request.studyName} 시작, ${request.remainMinutes.mean} 전입니다"
        }

        val topic = getMoitTopic(request.studyId)

        try {
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build()

            // data 전송이 함께 필요하다면, putData 로 함께 publish
            val msg = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .build()

            val response = firebaseMessaging.send(msg)
            logger.info("success to send notification : {}", response)
        } catch (e: Exception) {
            logger.error("Fail to send Message. topic-id : {}, title: {}, : [{}]", topic, title, e.toString())
        }
    }

    private fun getMoitTopic(moitId: Long): String {
        return TOPIC_MOIT_PREFIX + moitId
    }

    companion object {
        private const val STUDY_REMINDER_NOTIFICATION = "Study Remind Notification"
        private const val TOPIC_MOIT_PREFIX = "MOIT-"
    }
}
