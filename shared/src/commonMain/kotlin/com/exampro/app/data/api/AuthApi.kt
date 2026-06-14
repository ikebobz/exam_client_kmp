package com.exampro.app.data.api

import com.exampro.app.data.models.AuthResponse
import com.exampro.app.data.models.LoginRequest
import com.exampro.app.data.models.RegisterRequest
import com.exampro.app.data.models.UserProfile
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApi(private val client: HttpClient) {

    suspend fun login(request: LoginRequest): AuthResponse {
        return client.post("api/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        return client.post("api/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun logout() {
        client.get("api/logout")
    }

    suspend fun getProfile(): UserProfile {
        return client.get("api/auth/user").body()
    }
}