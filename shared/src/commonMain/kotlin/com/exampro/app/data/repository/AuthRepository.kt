package com.exampro.app.data.repository


import com.exampro.app.data.api.AuthApi
import com.exampro.app.data.api.DeviceApi
import com.exampro.app.data.api.DeviceRegistrationRequest
import com.exampro.app.data.models.AuthResponse
import com.exampro.app.data.models.DashboardStats
import com.exampro.app.data.models.LoginRequest
import com.exampro.app.data.models.RegisterRequest
import com.exampro.app.data.models.UserProfile
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.collections.remove

// Define an expect fun for the push token since Firebase is platform-specific
expect suspend fun getPushToken(): String?

class AuthRepository(
    private val authApi: AuthApi,
    private val deviceApi: DeviceApi,
    private val settings: Settings
) {
    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_LAST_EMAIL = "last_email"
        private const val KEY_LAST_PASSWORD = "last_password"
        private const val KEY_TOTAL_EXAMS = "total_exams"
        private const val KEY_TOTAL_SUBJECTS = "total_subjects"
        private const val KEY_TOTAL_QUESTIONS = "total_questions"
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val authResponse = authApi.login(LoginRequest(email, password))
            saveSession(authResponse.user.id, authResponse.user.email)
            saveCredentials(email, password)
            Result.success(authResponse)
        } catch (e: ClientRequestException) {
            // Handles 4xx errors
            val errorMsg = parseErrorBody(e.response)
            Result.failure(Exception(errorMsg))
        } catch (e: ServerResponseException) {
            // Handles 5xx errors
            Result.failure(Exception("Server error. Please try again later."))
        } catch (e: Exception) {
            Result.failure(Exception("Service is unreachable, please retry again later!!"))
        }
    }

    suspend fun register(email: String, password: String, firstName: String?, lastName: String?): Result<AuthResponse> {
        return try {
            val authResponse = authApi.register(RegisterRequest(email, password, firstName, lastName))
            saveSession(authResponse.user.id, authResponse.user.email)
            saveCredentials(email, password)
            Result.success(authResponse)
        } catch (e: ClientRequestException) {
            val errorMsg = parseErrorBody(e.response)
            Result.failure(Exception(errorMsg))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            clearSession()
            Result.success(Unit)
        } catch (e: Exception) {
            clearSession()
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<UserProfile> {
        return try {
            val profile = authApi.getProfile()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncDeviceToken() {
        try {
            if (!isLoggedIn()) return

            val token = getPushToken() ?: return
            deviceApi.registerDevice(DeviceRegistrationRequest(token = token))
        } catch (e: Exception) {
            // Log error using a KMP friendly way or ignore
            println("Error syncing device token: ${e.message}")
        }
    }

    // Logic for parsing JSON error messages using kotlinx.serialization
    private suspend fun parseErrorBody(response: HttpResponse): String {
        return try {
            val body = response.bodyAsText()
            val json = Json.parseToJsonElement(body).jsonObject
            json["message"]?.jsonPrimitive?.content ?: "An error occurred."
        } catch (e: Exception) {
            "An error occurred."
        }
    }

    fun isLoggedIn(): Boolean = settings.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getSavedUserId(): String? = settings.getStringOrNull(KEY_USER_ID)

    fun getSavedUserEmail(): String? = settings.getStringOrNull(KEY_USER_EMAIL)

    fun getLastEmail(): String? = settings.getStringOrNull(KEY_LAST_EMAIL)

    fun getLastPassword(): String? = settings.getStringOrNull(KEY_LAST_PASSWORD)

    private fun saveSession(userId: String, email: String) {
        settings[KEY_IS_LOGGED_IN] = true
        settings[KEY_USER_ID] = userId
        settings[KEY_USER_EMAIL] = email
    }

    private fun saveCredentials(email: String, password: String) {
        settings[KEY_LAST_EMAIL] = email
        settings[KEY_LAST_PASSWORD] = password
    }

    private fun clearSession() {
        settings[KEY_IS_LOGGED_IN] = false
        settings.remove(KEY_USER_ID)
        settings.remove(KEY_USER_EMAIL)
    }

    fun saveStats(stats: DashboardStats) {
        settings[KEY_TOTAL_EXAMS] = stats.totalExams
        settings[KEY_TOTAL_SUBJECTS] = stats.totalSubjects
        settings[KEY_TOTAL_QUESTIONS] = stats.totalQuestions
    }

    fun getCachedStats(): DashboardStats {
        return DashboardStats(
            totalExams = settings.getInt(KEY_TOTAL_EXAMS, 0),
            totalSubjects = settings.getInt(KEY_TOTAL_SUBJECTS, 0),
            totalQuestions = settings.getInt(KEY_TOTAL_QUESTIONS, 0)
        )
    }
}