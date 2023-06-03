package com.mashup.moit.domain.moit

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class NotificationPolicy(
    @Column(name = "is_remind_active_of_notification") val isRemindActive: Boolean,
    @Enumerated(EnumType.STRING) @Column(name = "remind_option_of_notification") val remindOption: RemindOption,
    @Enumerated(EnumType.STRING) @Column(name = "remind_level_of_notification") val remindLevel: RemindLevel
)

// 리마인드 시간 옵션
enum class RemindOption {
    BEFORE_TEN_MINUTE, BEFORE_TWENTY_MINUTE, AFTER_TEN_MINUTE
}

enum class RemindLevel {
    WEAK, NORMAL, HARD
}
