package com.exampro.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Subject(
    val id: Int,
    @SerialName("examId") val examId: Int,
    val name: String,
    val code: String,
    val description: String?,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("updatedAt") val updatedAt: String?
)