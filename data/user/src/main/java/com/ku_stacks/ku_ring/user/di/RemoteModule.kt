package com.ku_stacks.ku_ring.user.di

import com.ku_stacks.ku_ring.user.api.FeedbackClient
import com.ku_stacks.ku_ring.user.api.FeedbackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
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