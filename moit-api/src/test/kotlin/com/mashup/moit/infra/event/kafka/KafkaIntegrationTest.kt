package com.mashup.moit.infra.event.kafka

import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.banner.BannerService
import com.mashup.moit.domain.banner.update.MoitUnapprovedFineExistBannerUpdateRequest
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.notification.NotificationService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.infra.event.FineCreateEvent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

@SpringBootTest(
    classes = [
        KafkaAutoConfiguration::class,
        KafkaProducer::class,
        KafkaConsumer::class
    ]
)
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ExtendWith(MockitoExtension::class)
class KafkaIntegrationTest(
    private val kafkaProducer: KafkaProducer
) : KafkaContainerInitializer() {

    @MockBean
    lateinit var studyService: StudyService

    @MockBean
    lateinit var attendanceService: AttendanceService

    @MockBean
    lateinit var fineService: FineService

    @MockBean
    lateinit var bannerService: BannerService

    @MockBean
    lateinit var notificationService: NotificationService

    @Captor
    lateinit var moitUnapprovedFineExistBannerUpdateRequest: ArgumentCaptor<MoitUnapprovedFineExistBannerUpdateRequest>
    
    @Test
    fun name() {
        val fineId: Long = 1
        val updateRequest = MoitUnapprovedFineExistBannerUpdateRequest(fineId)

        
            kafkaProducer.produce(FineCreateEvent(fineId))
        
        // default 10초
//        val request: MoitUnapprovedFineExistBannerUpdateRequest = anyObject(MoitUnapprovedFineExistBannerUpdateRequest::class.java)
        verify(bannerService).update(updateRequest)
//        await().atMost(5, TimeUnit.SECONDS)
//            .untilAsserted {
//            }

        println("Complete")
    }

}
