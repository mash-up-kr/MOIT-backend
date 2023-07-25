package com.mashup.moit.infra.event

object KafkaEventTopic {
    const val MOIT_CREATE = "moit_create"
    const val FINE_CREATE_REQUEST = "fine_create_request"
    const val STUDY_INITIALIZE = "study_initialize"
    const val FINE_CREATE = "fine_create"
    const val FINE_APPROVE = "fine_approve"
}

object KafkaConsumerGroup {
    const val MOIT_CREATE_STUDY_CREATE = "moit_create_study_create"
    const val STUDY_INITIALIZE_BANNER_UPDATE = "study_initialize_banner_update"
    const val FINE_CREATE_BANNER_UPDATE = "fine_create_banner_update"
    const val FINE_APPROVE_BANNER_UPDATE = "fine_approve_banner_update"
}
