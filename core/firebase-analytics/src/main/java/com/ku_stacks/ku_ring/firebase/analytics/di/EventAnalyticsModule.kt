package com.ku_stacks.ku_ring.firebase.analytics.di

import android.content.Context
import com.ku_stacks.ku_ring.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.firebase.analytics.initializer.AmplitudeApiKey
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
        @ApplicationContext context: Context,
        @AmplitudeApiKey amplitudeApiKey: String,
    ): EventAnalytics = EventAnalytics(
        context = context,
        amplitudeApiKey = amplitudeApiKey,
    )
}
