package com.mashup.moit.domain.moit

enum class ScheduleRepeatCycle {
    NONE, ONE_WEEK, TWO_WEEK, THREE_WEEK, FOUR_WEEK;
}

enum class NotificationRemindOption {
    BEFORE_TEN_MINUTE, BEFORE_TWENTY_MINUTE, AFTER_TEN_MINUTE;
}

enum class NotificationRemindLevel {
    WEAK, NORMAL, HARD;
}
