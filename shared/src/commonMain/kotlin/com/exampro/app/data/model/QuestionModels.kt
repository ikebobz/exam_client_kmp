package com.exampro.app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    @SerialName("subjectId") val subjectId: Int,
    @SerialName("questionText") val questionText: String,
    @SerialName("imageUrl") val imageUrl: String?,
    val year: Int?,
    val difficulty: String?,
    val topic: String?,
    val isBookmarked: Boolean = false,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("updatedAt") val updatedAt: String?,
    val answers: List<Answer>? = null
)

@Serializable
data class Answer(
    val id: Int,
    @SerialName("questionId") val questionId: Int,
    @SerialName("answerText") val answerText: String,
    @SerialName("isCorrect") val isCorrect: Boolean,
    val explanation: String?,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("updatedAt") val updatedAt: String?
)