package com.ku_stacks.ku_ring.push.di

import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.push.repository.PushRepository
import com.ku_stacks.ku_ring.push.repository.PushRepositoryImpl
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
    fun providePushRepository(
        pushDao: PushDao
    ): PushRepository {
        return PushRepositoryImpl(pushDao)
    }
}