package com.example.pexel.di

import android.content.Context
import androidx.room.Room
import com.example.pexel.data.local.dao.FavoriteDao
import com.example.pexel.data.local.database.PexelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PexelDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PexelDatabase::class.java,
            "pexel_database"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(database: PexelDatabase): FavoriteDao = database.favoriteDao()
}
