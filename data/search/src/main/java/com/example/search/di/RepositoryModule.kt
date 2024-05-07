package com.example.search.di

import com.example.search.repository.SearchHistoryRepository
import com.example.search.repository.SearchHistoryRepositoryImpl
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
    abstract fun provideSearchHistoryRepository(repositoryImpl: SearchHistoryRepositoryImpl): SearchHistoryRepository
}
