package com.mashup.moit.domain.moit

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class NotificationPolicyColumns(
    @Column(name = "is_remind_active", nullable = false)
    val isRemindActive: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(name = "remind_option")
    val remindOption: NotificationRemindOption?,

    @Enumerated(EnumType.STRING)
    @Column(name = "remind_level")
    val remindLevel: NotificationRemindLevel?,
)
