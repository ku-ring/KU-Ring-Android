package com.ku_stacks.ku_ring.library.di

import com.ku_stacks.ku_ring.library.repository.LibraryRepository
import com.ku_stacks.ku_ring.library.repository.LibraryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideLibraryRepository(repositoryImpl: LibraryRepositoryImpl): LibraryRepository

}