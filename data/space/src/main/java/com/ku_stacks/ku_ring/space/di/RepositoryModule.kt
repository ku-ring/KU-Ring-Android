package com.ku_stacks.ku_ring.space.di

import com.ku_stacks.ku_ring.space.repository.KuringSpaceRepository
import com.ku_stacks.ku_ring.space.repository.KuringSpaceRepositoryImpl
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
    abstract fun provideKuringSpaceRepository(repositoryImpl: KuringSpaceRepositoryImpl): KuringSpaceRepository
}