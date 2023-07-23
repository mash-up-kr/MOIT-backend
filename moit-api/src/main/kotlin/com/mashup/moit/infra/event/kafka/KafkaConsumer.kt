package com.mashup.moit.infra.event.kafka

import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.FINE_CREATE_TOPIC
import com.mashup.moit.infra.event.FineCreateEvent
import com.mashup.moit.infra.event.MOIT_CREATE_TOPIC
import com.mashup.moit.infra.event.MoitCreateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
    private val studyService: StudyService,
    private val fineService: FineService,
) {
    private val log: Logger = LoggerFactory.getLogger(KafkaConsumer::class.java)

    @KafkaListener(topics = [MOIT_CREATE_TOPIC])
    fun consumeMoitCreateEvent(event: MoitCreateEvent) {
        log.debug("consumeMoitCreateEvent called: {}", event)
        studyService.createStudies(event.moitId)
    }

    @KafkaListener(topics = [FINE_CREATE_TOPIC])
    fun consumeFineCreateEvent(event: FineCreateEvent) {
        log.debug("consumeFineCreateEvent called: {}", event)
        fineService.create(event.attendanceId, event.moitId)
    }

}
