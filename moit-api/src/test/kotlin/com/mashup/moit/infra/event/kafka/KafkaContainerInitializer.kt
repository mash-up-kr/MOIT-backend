package com.mashup.moit.infra.event.kafka

import com.mashup.moit.infra.event.KafkaEventTopic
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
open class KafkaContainerInitializer {

    val kafkaAdmin = KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to kafka.bootstrapServers))
    
    companion object {
        @JvmStatic
        @Container
        val kafka: KafkaContainer = KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
        )

        @JvmStatic
        @DynamicPropertySource
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers") { kafka.bootstrapServers }
        }
    }
    
    init {
        kafkaAdmin.createOrModifyTopics(NewTopic(KafkaEventTopic.MOIT_CREATE, 1, 1))
        kafkaAdmin.createOrModifyTopics(NewTopic(KafkaEventTopic.MOIT_JOIN, 1, 1))
        kafkaAdmin.createOrModifyTopics(NewTopic(KafkaEventTopic.STUDY, 1, 1))
        kafkaAdmin.createOrModifyTopics(NewTopic(KafkaEventTopic.FINE, 1, 1))
        kafkaAdmin.createOrModifyTopics(NewTopic(KafkaEventTopic.NOTIFICATION, 1, 1))
    }

}
