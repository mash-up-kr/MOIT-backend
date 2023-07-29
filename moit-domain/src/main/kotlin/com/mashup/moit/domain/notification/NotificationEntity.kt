package com.mashup.moit.domain.notification

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "notification")
@Entity
data class NotificationEntity(
    @Column(name = "type", nullable = false)
    val type: NotificationType,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "body", nullable = false)
    val body: String,

    @Column(name = "url_link", nullable = false)
    val urlScheme: String,
) : BaseEntity()


enum class NotificationType {
    STUDY_SCHEDULED_NOTIFICATION,
    ATTENDANCE_START_NOTIFICATION,
    CHECK_FINE_NOTIFICATION,
}
