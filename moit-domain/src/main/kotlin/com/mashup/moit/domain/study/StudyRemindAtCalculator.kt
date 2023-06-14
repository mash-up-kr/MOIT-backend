package com.mashup.moit.domain.study

import com.mashup.moit.domain.moit.NotificationRemindOption
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class StudyRemindAtCalculator {
    fun calculate(
        studyStartAt: LocalDateTime,
        notificationRemindOption: NotificationRemindOption,
    ): LocalDateTime {
        return when (notificationRemindOption) {
            NotificationRemindOption.STUDY_DAY_10_AM -> LocalDateTime.of(studyStartAt.toLocalDate(), LocalTime.of(10, 0))
            NotificationRemindOption.BEFORE_1_HOUR -> studyStartAt.minus(Duration.ofHours(1))
            NotificationRemindOption.BEFORE_30_MINUTE -> studyStartAt.minus(Duration.ofMinutes(30))
            NotificationRemindOption.BEFORE_10_MINUTE -> studyStartAt.minus(Duration.ofMinutes(10))
        }
    }
}
