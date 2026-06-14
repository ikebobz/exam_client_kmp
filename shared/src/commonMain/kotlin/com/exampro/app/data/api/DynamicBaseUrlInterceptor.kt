package com.exampro.app.data.api

import com.exampro.app.data.repository.SettingsRepository
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.encodedPath
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// In KMP, we configure the HttpClient directly rather than creating a separate Interceptor class
fun HttpClientConfig<*>.installDynamicBaseUrl(settingsRepository: SettingsRepository) {
    defaultRequest {
        val newBaseUrl = runBlocking { settingsRepository.baseUrl.first() }
        if (newBaseUrl.isNotEmpty()) {
            // This replaces the URL host/port dynamically for every request
            url(newBaseUrl + this.url.encodedPath)
        }
    }
}