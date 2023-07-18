package com.mashup.moit.infra.event.kafka

import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.FINE_CREATE_TOPIC
import com.mashup.moit.infra.event.FineCreateEvent
import com.mashup.moit.infra.event.STUDY_CREATE_TOPIC
import com.mashup.moit.infra.event.StudyCreateEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
    private val studyService: StudyService,
    private val fineService: FineService,
) {

    @KafkaListener(topics = [STUDY_CREATE_TOPIC])
    fun consumeStudyCreateEvent(event: StudyCreateEvent) {
        studyService.createStudies(event.moitId!!)
    }

    @KafkaListener(topics = [FINE_CREATE_TOPIC])
    fun consumeFineCreateEvent(event: FineCreateEvent) {
        fineService.create(event.attendanceId!!, event.moitId!!)
    }

}
