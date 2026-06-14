package com.exampro.app.data.api

import com.exampro.app.data.models.Exam
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ExamApi(private val client: HttpClient) {

    suspend fun getExams(): List<Exam> {
        return client.get("api/exams") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getExam(id: Int): Exam {
        // Path parameters are handled via string templates in Ktor
        return client.get("api/exams/$id") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}