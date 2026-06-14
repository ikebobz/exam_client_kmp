package com.exampro.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BookmarkEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val questionId: Int,
    val createdAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
)