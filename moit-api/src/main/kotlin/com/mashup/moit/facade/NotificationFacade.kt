package com.mashup.moit.facade

import com.mashup.moit.controller.notification.dto.MyNotificationListResponse
import com.mashup.moit.controller.notification.dto.NotificationResponse
import com.mashup.moit.domain.notification.NotificationService
import org.springframework.stereotype.Component

@Component
class NotificationFacade(
    private val notificationService: NotificationService
) {
    fun getMyNotification(userId: Long): MyNotificationListResponse {
        val notificationResponseList = notificationService.getNotificationByUserId(userId)
            .map { NotificationResponse.of(it) }

        return MyNotificationListResponse(userId, notificationResponseList)
    }
}
