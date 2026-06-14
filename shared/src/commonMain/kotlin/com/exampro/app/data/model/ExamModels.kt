package com.exampro.app.data.models

import kotlinx.serialization.SerialName

data class Exam(
    val id: Int,
    val name: String,
    val code: String,
    val description: String?,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("updatedAt") val updatedAt: String?
)
