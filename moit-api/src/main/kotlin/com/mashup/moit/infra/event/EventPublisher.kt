package com.mashup.moit.infra.event

interface EventPublisher {
    fun publish(topic: String, data: String)
    fun publish(topic: String, event: MoitEvent)
}
