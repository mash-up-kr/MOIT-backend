package com.mashup.moit.infra.event

object KafkaEventTopic {
    const val MOIT_CREATE = "moit_create"
    const val MOIT_JOIN = "moit_join"
    const val STUDY = "study"
    const val FINE = "fine"
    const val NOTIFICATION = "notification"
}

object KafkaConsumerGroup {
    const val MOIT_CREATE_STUDY_CREATE = "moit_create_study_create"
    const val MOIT_JOIN_ATTENDANCE_INITIALIZE = "moit_join_attendance_initialize"
    const val STUDY = "study"
    const val FINE_EVENT = "fine"
    const val NOTIFICATION_CREATE = "notification_create"
}
