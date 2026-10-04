package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.firebase.analytics.initializer.AmplitudeApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsConfigModule {
    @Provides
    @AmplitudeApiKey
    fun provideAmplitudeApiKey(): String = BuildConfig.AMPLITUDE_API_KEY
}
