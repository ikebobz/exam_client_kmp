package com.exampro.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.exampro.app.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Removed Hilt @Singleton and @Inject
// 2. Removed Android Context. We now pass the DataStore directly.
class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val BASE_URL_KEY = stringPreferencesKey("base_url")
    private val MAX_PREFETCH_KEY = intPreferencesKey("max_prefetch")

    val baseUrl: Flow<String> = dataStore.data.map { preferences ->
        preferences[BASE_URL_KEY] ?: Constants.BASE_URL
    }

    val maxPrefetch: Flow<Int> = dataStore.data.map { preferences ->
        preferences[MAX_PREFETCH_KEY] ?: Constants.DEFAULT_MAX_PREFETCH_QUESTIONS
    }

    suspend fun updateBaseUrl(newBaseUrl: String) {
        dataStore.edit { preferences ->
            preferences[BASE_URL_KEY] = if (newBaseUrl.endsWith("/")) newBaseUrl else "$newBaseUrl/"
        }
    }

    suspend fun updateMaxPrefetch(value: Int) {
        dataStore.edit { preferences ->
            preferences[MAX_PREFETCH_KEY] = value
        }
    }

    suspend fun resetBaseUrl() {
        dataStore.edit { preferences ->
            preferences.remove(BASE_URL_KEY)
        }
    }
}