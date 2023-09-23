package com.ku_stacks.ku_ring.user.di

import com.ku_stacks.ku_ring.user.local.BlackUserDao
import com.ku_stacks.ku_ring.user.repository.UserRepository
import com.ku_stacks.ku_ring.user.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepository(
        blackUserDao: BlackUserDao
    ): UserRepository {
        return UserRepositoryImpl(blackUserDao)
    }
}