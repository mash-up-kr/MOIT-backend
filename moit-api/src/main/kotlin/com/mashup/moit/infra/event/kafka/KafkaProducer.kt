package com.mashup.moit.infra.event.kafka

import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.MoitEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, Any>) : EventProducer {
    override fun produce(event: MoitEvent) {
        kafkaTemplate.send(event.getTopic(), event)
    }
}
