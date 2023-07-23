package com.mashup.moit.infra.event.kafka

import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.STUDY_CREATE_TOPIC
import com.mashup.moit.infra.event.StudyCreateEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
    private val studyService: StudyService,
) {
    
    @KafkaListener(topics = [STUDY_CREATE_TOPIC])
    fun consumeCreateStudyEvent(event: StudyCreateEvent) {
        studyService.createStudies(event.moitId!!)
    }
    
}
