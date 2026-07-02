package com.exampro.app.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.exampro.app.utils.AndroidContext


actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    //TODO("Not yet implemented")
    val ctx = AndroidContext.instance
    val dbFile = ctx.getDatabasePath("exampro_db.db")

    return Room.databaseBuilder<AppDatabase>(
        context = ctx,
        name = dbFile.absolutePath)
}
