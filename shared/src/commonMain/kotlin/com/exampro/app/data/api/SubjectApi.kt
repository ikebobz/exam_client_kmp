package com.exampro.app.data.api

import com.exampro.app.data.models.Subject
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class SubjectApi(private val client: HttpClient) {

    suspend fun getSubjects(): List<Subject> {
        return client.get("api/subjects") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getSubject(id: Int): Subject {
        return client.get("api/subjects/$id") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}