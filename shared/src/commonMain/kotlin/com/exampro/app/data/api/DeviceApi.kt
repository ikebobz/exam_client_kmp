package com.exampro.app.data.api


import io.ktor.client.*
import io.ktor.client.HttpClient // This works on Android AND iOS
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 1. Use @Serializable for Multiplatform support
@Serializable
data class DeviceRegistrationRequest(
    @SerialName("deviceToken") val token: String,
    val platform: String = "android" // You can later make this dynamic based on the platform
)

// 2. Retrofit Interfaces don't exist in KMP. Use a Class with a Ktor HttpClient.
class DeviceApi(private val client: HttpClient) {

    suspend fun registerDevice(request: DeviceRegistrationRequest) {
        client.post("api/devices/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}