package com.mashup.moit.infra.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.sample.controller.sample.dto.SampleNotificationRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FCMNotificationService(
    private val firebaseMessaging: FirebaseMessaging
) {
    private val logger = LoggerFactory.getLogger(FCMNotificationService::class.java)

    fun sendTopicSampleNotification(request: SampleNotificationRequest) {
        val title = STUDY_REMINDER_NOTIFICATION

        val body = when (request.remainMinutes) {
            NotificationRemindOption.STUDY_DAY_10_AM -> "오늘은 ${request.targetName} 스터디가 있는 날이에요"
            else -> "${request.targetName} 시작, ${request.remainMinutes.mean} 전입니다"
        }

        val topic = getStudyTopic(request.targetId)

        try {
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

            // data 전송이 함께 필요하다면, putData 로 함께 publish
            val msg = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .build();

            firebaseMessaging.send(msg)
        } catch (e: Exception) {
            logger.error(
                "Fail to send Message. topic-id : {}, title: {}, : [{}]",
                topic, title, e.toString()
            )
        }
    }

    fun getStudyTopic(studyId: Long): String {
        return "study-$studyId"
    }

    companion object {
        private const val STUDY_REMINDER_NOTIFICATION = "Study Remind Notification"
    }
}
