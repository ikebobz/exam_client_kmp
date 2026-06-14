package com.exampro.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    @SerialName("firstName") val firstName: String? = null,
    @SerialName("lastName") val lastName: String? = null
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val user: AuthUser
)

@Serializable
data class AuthUser(
    val id: String,
    val email: String
)

@Serializable
data class UserProfile(
    val id: String,
    val email: String?,
    @SerialName("first_name") val firstName: String?,
    @SerialName("last_name") val lastName: String?,
    @SerialName("profile_image_url") val profileImageUrl: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?
)