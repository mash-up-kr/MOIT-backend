package com.mashup.moit.domain.notification

import java.time.LocalDateTime

data class Notification(
    val id: Long,
    val userId: Long,
    val type: NotificationType,
    val title: String,
    val body: String,
    val urlScheme: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(notificationEntity: NotificationEntity) = Notification(
            id = notificationEntity.id,
            userId = notificationEntity.userId,
            type = notificationEntity.type,
            title = notificationEntity.title,
            body = notificationEntity.body,
            urlScheme = notificationEntity.urlScheme,
            createdAt = notificationEntity.createdAt,
        )
    }
}
        
