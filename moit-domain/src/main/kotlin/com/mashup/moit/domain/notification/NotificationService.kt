package com.mashup.moit.domain.notification

import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository
) {
    fun getNotificationByUserId(userId: Long): List<Notification> {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .map { Notification.of(it) }
    }
}
