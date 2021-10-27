package com.ku_stacks.ku_ring.di

import android.content.Context
import com.ku_stacks.ku_ring.util.PreferenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Singleton
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ) = PreferenceUtil(context)
}