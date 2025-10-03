package com.ku_stacks.ku_ring.academicevent.di

import com.ku_stacks.ku_ring.academicevent.repository.AcademicEventRepositoryImpl
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
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
    abstract fun bindsAcademicEventRepository(
        repositoryImpl: AcademicEventRepositoryImpl,
    ): AcademicEventRepository
}