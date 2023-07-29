package com.mashup.moit.controller.notification.dto

import com.mashup.moit.domain.notification.Notification
import com.mashup.moit.domain.notification.NotificationType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "내 알림 list Response")
data class MyNotificationListResponse(
    @Schema(description = "user id")
    val userId: Long,
    @Schema(description = "알림 list")
    val notificationList: List<NotificationResponse>,
)

@Schema(description = "알림 Response")
data class NotificationResponse(
    @Schema(description = "notification id")
    val id: Long,
    @Schema(description = "user id")
    val userId: Long,
    @Schema(description = "알림 타입")
    val type: NotificationType,
    @Schema(description = "알림 제목")
    val title: String,
    @Schema(description = "알림 내용")
    val body: String,
    @Schema(description = "딥링크")
    val urlScheme: String,
    @Schema(description = "알림 시각")
    val notificationAt: LocalDateTime,
) {
    companion object {
        fun of(notification: Notification) = NotificationResponse(
            id = notification.id,
            userId = notification.userId,
            type = notification.type,
            title = notification.title,
            body = notification.body,
            urlScheme = notification.urlScheme,
            notificationAt = notification.createdAt
        )
    }
}
