package com.mashup.moit.domain.notification

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "notification")
@Entity
data class NotificationEntity(
    @Column("type", nullable = false)
    val type: NotificationType,

    @Column("user_id", nullable = false)
    val userId: Long,

    @Column("title", nullable = false)
    val title: String,

    @Column("body", nullable = false)
    val body: String,

    @Column("url_link", nullable = false)
    val urlScheme: String,
) : BaseEntity() {
    companion object {
        
    }
}


enum class NotificationType {
    STUDY_SCHEDULED_NOTIFICATION,
    ATTENDANCE_START_NOTIFICATION,
    CHECK_FINE_NOTIFICATION,
}
