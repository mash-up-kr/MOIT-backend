package com.mashup.moit.infra.fcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.jasypt.encryption.StringEncryptor
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
    private val stringEncryptor: StringEncryptor
) {
    val logger: Logger = LoggerFactory.getLogger(FCMConfig::class.java)

    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        val resource = ClassPathResource(googleApplicationCredentials)

        // JSON 파일을 문자열로 읽고 필요한 값을 복호화하여 사용
        val jsonContent = resource.inputStream.bufferedReader().use { it.readText() }
        val decryptedJsonContent = decryptEncryptedValues(jsonContent)

        val firebaseAppList = FirebaseApp.getApps()

        val firebaseApp = if (firebaseAppList != null && firebaseAppList.isNotEmpty()) {
            firebaseAppList.find { it.name.equals(FirebaseApp.DEFAULT_APP_NAME) }
        } else {
            val options: FirebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(decryptedJsonContent.byteInputStream()))
                .build()

            FirebaseApp.initializeApp(options)
        }

        logger.info("FirebaseApp initialization complete")
        return FirebaseMessaging.getInstance(firebaseApp)
    }

    private fun decryptEncryptedValues(jsonContent: String): String {
        val regex = Regex("""ENC\((.*?)\)""")  // ENC() 패턴을 정규식으로 검색
        val decryptedContent = regex.replace(jsonContent) { matchResult ->
            val encryptedValue = matchResult.groupValues[1]
            val decryptedValue = stringEncryptor.decrypt(encryptedValue)
            decryptedValue
        }

        return decryptedContent
    }
}
