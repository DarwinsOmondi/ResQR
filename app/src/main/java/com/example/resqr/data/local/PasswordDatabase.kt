package com.example.resqr.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PasswordEntity::class], version = 1, exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao

    companion object {
        @Volatile private var INSTANCE: PasswordDatabase? = null

        fun getDatabase(context: Context): PasswordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PasswordDatabase::class.java,
                    "password_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}