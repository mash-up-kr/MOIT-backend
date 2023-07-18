package com.mashup.moit.infra.event

fun interface EventProducer {
    fun produce(event: MoitEvent)
}
