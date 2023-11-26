package com.ku_stacks.ku_ring.push.di

import com.ku_stacks.ku_ring.push.repository.PushRepository
import com.ku_stacks.ku_ring.push.repository.PushRepositoryImpl
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
    abstract fun providePushRepository(repositoryImpl: PushRepositoryImpl): PushRepository
}