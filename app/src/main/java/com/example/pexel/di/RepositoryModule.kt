package com.example.pexel.di

import com.example.pexel.data.remote.repository.PexelRepository
import com.example.pexel.data.remote.repository.PexelRepositoryImpl
import com.example.pexel.data.repository.FavoriteRepositoryImpl
import com.example.pexel.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPexelRepository(
        pexelRepositoryImpl: PexelRepositoryImpl
    ): PexelRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}
