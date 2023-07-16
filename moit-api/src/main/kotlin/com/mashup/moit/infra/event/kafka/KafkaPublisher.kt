package com.mashup.moit.infra.event.kafka

import com.mashup.moit.infra.event.EventPublisher
import com.mashup.moit.infra.event.MoitEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaPublisher(private val kafkaTemplate: KafkaTemplate<String, String>) : EventPublisher {

    override fun publish(topic: String, data: String) {
        kafkaTemplate.send(topic, data)
    }

    override fun publish(topic: String, event: MoitEvent) {
        TODO("Not yet implemented")
    }

}
