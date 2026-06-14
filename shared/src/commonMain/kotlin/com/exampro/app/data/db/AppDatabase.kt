package com.exampro.app.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exampro.app.data.db.dao.*
import com.exampro.app.data.db.entities.*

@Database(
    entities = [
        ExamEntity::class,
        SubjectEntity::class,
        QuestionEntity::class,
        AnswerEntity::class,
        StudyProgressEntity::class,
        BookmarkEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
// The @ConstructedBy annotation is required for Room KMP to generate the initialization code
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun examDao(): ExamDao
    abstract fun subjectDao(): SubjectDao
    abstract fun questionDao(): QuestionDao
    abstract fun studyProgressDao(): StudyProgressDao
    abstract fun bookmarkDao(): BookmarkDao
}

// 1. Define an empty expect object for the constructor
expect object AppDatabaseConstructor : androidx.room.RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

// 2. Define an 'expect' function to get the builder on each platform
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

// 3. Helper function to initialize the database in a common way
fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}