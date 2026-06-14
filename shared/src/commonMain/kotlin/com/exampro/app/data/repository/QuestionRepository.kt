package com.exampro.app.data.repository

import com.exampro.app.data.db.dao.BookmarkDao
import com.exampro.app.data.db.dao.QuestionDao
import com.exampro.app.data.db.entities.BookmarkEntity
import com.exampro.app.data.models.Answer
import com.exampro.app.data.models.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Removed @Singleton and @Inject (Koin handles this)
class QuestionRepository(
    private val questionDao: QuestionDao,
    private val bookmarkDao: BookmarkDao,
    private val authRepository: AuthRepository
) {
    fun getBookmarkedQuestionsFlow(): Flow<List<Question>> {
        val userId = authRepository.getSavedUserId() ?: ""
        return bookmarkDao.getBookmarkedQuestions(userId).map { entities ->
            val questionIds = entities.map { it.id }
            val allAnswers = if (questionIds.isNotEmpty()) {
                questionDao.getAnswersByQuestions(questionIds)
            } else {
                emptyList()
            }
            val answersByQuestion = allAnswers.groupBy { it.questionId }
            entities.map { entity ->
                entity.toModel(
                    answers = answersByQuestion[entity.id]?.map { it.toModel() },
                    isBookmarked = true
                )
            }
        }
    }

    fun getQuestionsBySubjectFlow(subjectId: Int): Flow<List<Question>> {
        val userId = authRepository.getSavedUserId() ?: ""
        return questionDao.getQuestionsBySubject(subjectId).map { entities ->
            // Use a Set for O(1) lookup performance on large lists
            val bookmarkedIds = bookmarkDao.getBookmarkedQuestionIds(userId).toSet()
            entities.map { it.toModel(isBookmarked = bookmarkedIds.contains(it.id)) }
        }
    }

    suspend fun getQuestion(id: Int): Result<Question> {
        val userId = authRepository.getSavedUserId() ?: ""
        return try {
            val cached = questionDao.getQuestionById(id)
            if (cached != null) {
                val answers = questionDao.getAnswersByQuestion(id)
                val isBookmarked = bookmarkDao.getBookmark(userId, id) != null
                Result.success(
                    cached.toModel(
                        answers = answers.map { it.toModel() },
                        isBookmarked = isBookmarked
                    )
                )
            } else {
                Result.failure(Exception("Question not found in local database"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleBookmark(question: Question): Boolean {
        val userId = authRepository.getSavedUserId() ?: return false
        val existing = bookmarkDao.getBookmark(userId, question.id)

        return if (existing != null) {
            bookmarkDao.deleteBookmark(userId, question.id)
            false
        } else {
            bookmarkDao.insertBookmark(BookmarkEntity(userId = userId, questionId = question.id))
            true
        }
    }

    // --- Internal Mappings ---
    // These convert database entities (internal) to UI models (shared)

    private fun com.exampro.app.data.db.entities.QuestionEntity.toModel(
        answers: List<Answer>? = null,
        isBookmarked: Boolean = false
    ): Question = Question(
        id = id,
        subjectId = subjectId,
        questionText = questionText,
        imageUrl = imageUrl,
        year = year,
        difficulty = difficulty,
        topic = topic,
        isBookmarked = isBookmarked,
        createdAt = createdAt,
        updatedAt = updatedAt,
        answers = answers
    )

    private fun com.exampro.app.data.db.entities.AnswerEntity.toModel(): Answer = Answer(
        id = id,
        questionId = questionId,
        answerText = answerText,
        isCorrect = isCorrect,
        explanation = explanation,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}