package com.exampro.app.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
actual suspend fun getPushToken(): String? {
    return try {
        // This calls the Android-specific Firebase SDK
        FirebaseMessaging.getInstance().token.await()
    } catch (e: Exception) {
        // Log the error for Android specifically if needed
        e.printStackTrace()
        null
    }
}