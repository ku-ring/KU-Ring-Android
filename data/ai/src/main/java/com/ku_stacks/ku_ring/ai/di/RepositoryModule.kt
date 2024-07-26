package com.ku_stacks.ku_ring.ai.di

import com.ku_stacks.ku_ring.ai.repository.KuringBotRepository
import com.ku_stacks.ku_ring.ai.repository.KuringBotRepositoryImpl
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
    abstract fun bindKuringBotMessageRepository(repositoryImpl: KuringBotRepositoryImpl): KuringBotRepository
}