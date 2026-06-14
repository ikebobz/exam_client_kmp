package com.exampro.app.data.api

import com.exampro.app.data.models.Question
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class QuestionApi(private val client: HttpClient) {

    suspend fun getQuestions(): List<Question> {
        return client.get("api/questions") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getQuestion(id: Int): Question {
        // Ktor uses string templates for path parameters
        return client.get("api/questions/$id") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}