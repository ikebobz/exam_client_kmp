package com.exampro.app.data.api

import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.client.plugins.*

// Ktor handles the "Set-Cookie" and "Cookie" headers automatically
// if you install the HttpCookies plugin.
fun HttpClientConfig<*>.installSessionCookies(customStorage: CookiesStorage? = null) {
    install(HttpCookies) {
        // If storage is null, it stays in memory (cleared when app closes)
        // You can implement a custom persistent storage that works on both iOS/Android
        storage = customStorage ?: AcceptAllCookiesStorage()
    }
}

/**
 * Example of how you would clear cookies in KMP
 */
/*suspend fun HttpClient.clearCookies() {
    val cachingProperty = this.plugin(HttpCookies)
    cachingProperty.storage.get(Url("")).forEach {
        // Logic to clear storage
    }
}*/