package com.mashup.moit.infra.event.kafka

import com.mashup.moit.infra.event.EventTopic
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class KafkaConsumer {

    @KafkaListener(topics = [EventTopic.MOIT_INIT])
    fun test(data: String) {
        println("test data : $data")
    }

}
