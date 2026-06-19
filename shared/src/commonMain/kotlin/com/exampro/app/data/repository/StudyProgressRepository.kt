package com.exampro.app.data.repository

import com.exampro.app.data.db.dao.StudyProgressDao
import com.exampro.app.data.db.entities.StudyProgressEntity
import kotlinx.coroutines.flow.Flow
//import kotlinx.datetime.Clock // KMP replacement for Date
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// 1. Removed @Singleton and @Inject (Koin handles this)
class StudyProgressRepository(
    private val studyProgressDao: StudyProgressDao
) {
    // 2. SimpleDateFormat is replaced by kotlinx-datetime logic
    // Clock.System.now().toString() produces ISO-8601 format by default

    fun getAllProgressFlow(): Flow<List<StudyProgressEntity>> {
        return studyProgressDao.getAllProgress()
    }

    fun getProgressBySubjectFlow(subjectId: Int): Flow<List<StudyProgressEntity>> {
        return studyProgressDao.getProgressBySubject(subjectId)
    }

    @OptIn(ExperimentalTime::class)
    suspend fun saveQuizResult(
        subjectId: Int,
        totalQuestions: Int,
        correctAnswers: Int
    ): Long {
        val score = if (totalQuestions > 0) {
            (correctAnswers.toFloat() / totalQuestions.toFloat()) * 100f
        } else {
            0f
        }

        val progress = StudyProgressEntity(
            subjectId = subjectId,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            score = score,
            // 3. Generates "yyyy-MM-dd'T'HH:mm:ss.SSSZ" automatically
            completedAt = kotlin.time.Clock.System.now().toEpochMilliseconds().toString()
        )

        return studyProgressDao.insert(progress)
    }

    suspend fun getAverageScoreBySubject(subjectId: Int): Float {
        return studyProgressDao.getAverageScoreBySubject(subjectId) ?: 0f
    }

    suspend fun getOverallAverageScore(): Float {
        return studyProgressDao.getOverallAverageScore() ?: 0f
    }

    suspend fun getTotalQuizCount(): Int {
        return studyProgressDao.getTotalQuizCount()
    }

    suspend fun getTotalQuestionsAnswered(): Int {
        return studyProgressDao.getTotalQuestionsAnswered() ?: 0
    }

    suspend fun getTotalCorrectAnswers(): Int {
        return studyProgressDao.getTotalCorrectAnswers() ?: 0
    }

    suspend fun getAllProgress(): List<StudyProgressEntity> {
        return studyProgressDao.getAllProgressList()
    }

    suspend fun getProgressBySubject(subjectId: Int): List<StudyProgressEntity> {
        return studyProgressDao.getProgressBySubjectList(subjectId)
    }

    suspend fun clearAllProgress() {
        studyProgressDao.deleteAll()
    }
}