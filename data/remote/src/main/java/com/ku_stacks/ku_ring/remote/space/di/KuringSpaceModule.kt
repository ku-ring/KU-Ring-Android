package com.ku_stacks.ku_ring.remote.space.di

import com.ku_stacks.ku_ring.remote.space.KuringSpaceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KuringSpaceModule {
    @Provides
    @Singleton
    fun provideKuringSpaceService(@Named("KuringSpace") retrofit: Retrofit): KuringSpaceService {
        return retrofit.create(KuringSpaceService::class.java)
    }
}