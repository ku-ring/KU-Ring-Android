package com.ku_stacks.kustack.di

import com.ku_stacks.kustack.data.api.ITunesClient
import com.ku_stacks.kustack.repository.ITunesRepository
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
    fun provideIRepository(
        itunesClient: ITunesClient
    ): ITunesRepository {
        return ITunesRepository(itunesClient)
    }
}