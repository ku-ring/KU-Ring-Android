package com.ku_stacks.ku_ring.search.di

import com.ku_stacks.ku_ring.search.repository.SearchHistoryRepository
import com.ku_stacks.ku_ring.search.repository.SearchHistoryRepositoryImpl
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
