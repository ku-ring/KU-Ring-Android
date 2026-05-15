package com.ku_stacks.ku_ring.remote.club.di

import com.ku_stacks.ku_ring.remote.club.ClubClient
import com.ku_stacks.ku_ring.remote.club.ClubService
import com.ku_stacks.ku_ring.remote.util.Default
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClubModule {
    @Provides
    @Singleton
    fun provideClubService(@Default retrofit: Retrofit): ClubService {
        return retrofit.create(ClubService::class.java)
    }

    @Provides
    @Singleton
    fun provideClubClient(clubService: ClubService): ClubClient {
        return ClubClient(clubService)
    }
}