package com.mashup.moit.infra.fcm

import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.stereotype.Service

@Service
class FCMNotificationService(
    private val firebaseMessaging: FirebaseMessaging
) {
    
}
