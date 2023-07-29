package com.mashup.moit.controller.notification

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.controller.notification.dto.MyNotificationListResponse
import com.mashup.moit.facade.NotificationFacade
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Notification", description = "Notification 관련 Api 입니다")
@RequestMapping("/api/v1/notification")
@RestController
class NotificationController(
    private val notificationFacade: NotificationFacade
) {
    @Operation(summary = "My Notification List API", description = "내 알림 List 조회")
    @GetMapping
    fun getMyNotifications(
        @GetAuth userInfo: UserInfo,
    ): MoitApiResponse<MyNotificationListResponse> {
        return MoitApiResponse.success(notificationFacade.getMyNotification(userInfo.id))
    }
}
