package com.gramasuvidha.portal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gramasuvidha.portal.data.local.dao.FeedbackDao
import com.gramasuvidha.portal.data.local.dao.ProjectDao
import com.gramasuvidha.portal.data.local.dao.UserDao
import com.gramasuvidha.portal.data.local.entities.Feedback
import com.gramasuvidha.portal.data.local.entities.ProjectEntity
import com.gramasuvidha.portal.data.local.entities.UserEntity

@Database(entities = [ProjectEntity::class, Feedback::class, UserEntity::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun feedbackDao(): FeedbackDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grama_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
