package com.ku_stacks.ku_ring.remote.user.di

import com.ku_stacks.ku_ring.remote.user.FeedbackClient
import com.ku_stacks.ku_ring.remote.user.FeedbackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideFeedbackService(@Named("Default") retrofit: Retrofit): FeedbackService {
        return retrofit.create(FeedbackService::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedbackClient(feedbackService: FeedbackService): FeedbackClient {
        return FeedbackClient(feedbackService)
    }
}