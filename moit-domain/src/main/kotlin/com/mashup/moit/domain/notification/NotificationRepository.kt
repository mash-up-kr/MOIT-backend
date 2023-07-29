package com.mashup.moit.domain.notification

import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<NotificationEntity, Long> {
    fun findByUserIdOrderByCreatedAt(userId: Long): List<NotificationEntity>
}
