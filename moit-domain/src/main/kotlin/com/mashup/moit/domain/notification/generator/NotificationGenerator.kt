package com.mashup.moit.domain.notification.generator

import com.mashup.moit.domain.notification.NotificationEntity
import com.mashup.moit.domain.notification.NotificationEvent

interface NotificationGenerator {
    fun support(event: NotificationEvent): Boolean
    fun generate(event: NotificationEvent): List<NotificationEntity>
}
