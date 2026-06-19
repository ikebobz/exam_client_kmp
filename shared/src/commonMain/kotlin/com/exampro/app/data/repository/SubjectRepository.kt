package com.exampro.app.data.repository

import com.exampro.app.data.db.dao.SubjectDao
import com.exampro.app.data.models.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Removed @Singleton and @Inject. Koin will handle the lifecycle.
class SubjectRepository(
    private val subjectDao: SubjectDao
) {
    fun getSubjectsFlow(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getSubjectsByExamFlow(examId: Int): Flow<List<Subject>> {
        return subjectDao.getSubjectsByExam(examId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun getSubject(id: Int): Result<Subject> {
        return try {
            val cached = subjectDao.getSubjectById(id)
            if (cached != null) {
                Result.success(cached.toModel())
            } else {
                Result.failure(Exception("Subject not found in local database"))
            }
        } catch (e: Exception) {
            // Wrapping DB exceptions in a Result for safe cross-platform handling
            Result.failure(e)
        }
    }

    // Extension function to map Database Entity to Shared UI Model
    private fun com.exampro.app.data.db.entities.SubjectEntity.toModel(): Subject = Subject(
        id = id,
        examId = examId,
        name = name,
        code = code,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}