package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.data.api.SendbirdClient
import com.ku_stacks.ku_ring.repository.*
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
    fun provideSendbirdRepository(
        sendbirdClient: SendbirdClient
    ): SendbirdRepository {
        return SendbirdRepositoryImpl(sendbirdClient)
    }

}