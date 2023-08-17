//package com.mashup.moit.infra.event.kafka
//
//import com.mashup.moit.domain.attendance.AttendanceService
//import com.mashup.moit.domain.study.StudyService
//import org.mockito.Mockito.mock
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//
//@Configuration
//class KafkaConsumerMockConfiguration {
//    
//    @Bean
//    @Primary
//    fun kafkaConsumer(): KafkaConsumer {
//        return KafkaConsumer(
//            mock(StudyService::class.java),
//            mock(AttendanceService::class.java),
//            mock()
//        )
//    }
//}
