package com.mashup.moit.domain.notification

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val notificationGenerator: List<NotificationGenerator>,
) {
    fun getNotificationByUserId(userId: Long): List<Notification> {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .map { Notification.of(it) }
    }

    @Transactional
    fun save(event: NotificationEvent) {
        val notificationEntities = notificationGenerator.first { it.support(event) }
            .generate(event)

        notificationRepository.saveAll(notificationEntities)
    }
}
