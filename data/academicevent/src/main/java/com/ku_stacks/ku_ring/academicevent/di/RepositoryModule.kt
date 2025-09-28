package com.ku_stacks.ku_ring.academicevent.di

import com.ku_stacks.ku_ring.academicevent.repository.AcademicEventRepository
import com.ku_stacks.ku_ring.academicevent.repository.AcademicEventRepositoryImpl
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
    abstract fun provideAcademicEventRepository(
        repositoryImpl: AcademicEventRepositoryImpl,
    ): AcademicEventRepository
}