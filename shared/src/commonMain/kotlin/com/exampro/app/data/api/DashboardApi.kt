package com.exampro.app.data.api

import com.exampro.app.data.models.DashboardStats
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class DashboardApi(private val client: HttpClient) {

    suspend fun getStats(): DashboardStats {
        return client.get("api/dashboard/stats") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}
