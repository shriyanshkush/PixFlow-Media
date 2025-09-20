package com.example.pexel.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.pexel.data.local.dao.FavoriteDao
import com.example.pexel.data.local.entities.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PexelDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: PexelDatabase? = null

        fun getDatabase(context: Context): PexelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PexelDatabase::class.java,
                    "pexel_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}