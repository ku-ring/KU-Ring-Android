package com.ku_stacks.ku_ring.remote.verification.di

import com.ku_stacks.ku_ring.remote.verification.VerificationClient
import com.ku_stacks.ku_ring.remote.verification.VerificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object VerificationModule {
    @Provides
    @Singleton
    fun provideVerificationService(@Named("Default") retrofit: Retrofit): VerificationService =
        retrofit.create(VerificationService::class.java)
}
