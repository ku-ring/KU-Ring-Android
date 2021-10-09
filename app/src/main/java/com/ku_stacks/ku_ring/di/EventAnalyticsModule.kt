package com.ku_stacks.ku_ring.di

import android.content.Context
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventAnalyticsModule {

    @Singleton
    @Provides
    fun provideEventAnalytics(
        @ApplicationContext context: Context
    ) = EventAnalytics(context)
}