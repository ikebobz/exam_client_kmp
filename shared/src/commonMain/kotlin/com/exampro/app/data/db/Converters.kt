package com.exampro.app.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

class Converters {

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun fromTimestamp(value: String?): Instant? {
        return value?.let {
            try {
                // Instant.parse automatically handles ISO 8601 strings
                // like "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
                Instant.parse(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun dateToTimestamp(date: Instant?): String? {
        // .toString() on an Instant produces a standard ISO 8601 string
        return date?.toString()
    }
}