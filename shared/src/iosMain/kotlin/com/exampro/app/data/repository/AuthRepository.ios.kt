package com.exampro.app.data.repository

actual suspend fun getPushToken(): String? {
    // If you haven't set up Firebase for iOS yet, return null
    // or implement the native iOS APNs token logic here.
    return null
}