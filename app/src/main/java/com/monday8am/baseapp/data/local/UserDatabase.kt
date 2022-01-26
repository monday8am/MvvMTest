package com.monday8am.baseapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.monday8am.baseapp.data.local.user.CachedUser
import com.monday8am.baseapp.data.local.user.UserDao

@Database(
    entities = [
        CachedUser::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        private const val DATABASE_NAME = "user_db"

        fun getInstance(context: Context): UserDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(appContext: Context): UserDatabase {
            return Room.databaseBuilder(appContext, UserDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration() // Data is cache, so it is OK to delete
                .build()
        }
    }
}
