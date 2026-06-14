package com.exampro.app.data.repository

import com.exampro.app.data.api.ExamApi
import com.exampro.app.data.db.dao.ExamDao
import com.exampro.app.data.models.Exam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Removed @Singleton and @Inject (Koin will handle this)
class ExamRepository(
    private val examApi: ExamApi, // Added the API to handle network fetching
    private val examDao: ExamDao
) {
    /**
     * Returns a Flow of exams from the local database.
     * This works on Android and iOS using Room KMP or SQLDelight.
     */
    fun getExamsFlow(): Flow<List<Exam>> {
        return examDao.getAllExams().map { entities ->
            entities.map { it.toModel() }
        }
    }

    /**
     * Tries to get an exam from the local cache.
     */
    suspend fun getExam(id: Int): Result<Exam> {
        return try {
            val cached = examDao.getExamById(id)
            if (cached != null) {
                Result.success(cached.toModel())
            } else {
                Result.failure(Exception("Exam not found in local database"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Example of a KMP "Sync" method: Fetch from network and save to local DB
     */
    suspend fun refreshExams(): Result<Unit> {
        return try {
            val remoteExams = examApi.getExams()
            // Here you would convert remote models to entities and save to DAO
            // examDao.insertAll(remoteExams.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Extension function for mapping database entities to UI models
    private fun com.exampro.app.data.db.entities.ExamEntity.toModel(): Exam = Exam(
        id = id,
        name = name,
        code = code,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}