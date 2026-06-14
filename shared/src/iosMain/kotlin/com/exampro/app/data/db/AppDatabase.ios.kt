package com.exampro.app.data.db

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

actual object AppDatabaseConstructor :
    RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        TODO("Not yet implemented")
    }
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    TODO("Not yet implemented")
}