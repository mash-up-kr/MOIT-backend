package com.mashup.moit.infra.fcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FCMConfig(
    @Value("\${fcm.certification}")
    private val googleApplicationCredentials: String,
) {
    val logger: Logger = LoggerFactory.getLogger(FCMConfig::class.java)
    
    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        val resource = ClassPathResource(googleApplicationCredentials)
        val firebaseAppList = FirebaseApp.getApps()

        val firebaseApp = if (firebaseAppList != null && firebaseAppList.isNotEmpty()) {
            firebaseAppList.find { it.name.equals(FirebaseApp.DEFAULT_APP_NAME) }
        } else {
            val options: FirebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.inputStream))
                .build()

            FirebaseApp.initializeApp(options)
        }

        logger.info("FirebaseApp initialization complete")
        return FirebaseMessaging.getInstance(firebaseApp)
    }
}
