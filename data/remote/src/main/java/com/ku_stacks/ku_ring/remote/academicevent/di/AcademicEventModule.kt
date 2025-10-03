package com.ku_stacks.ku_ring.remote.academicevent.di

import com.ku_stacks.ku_ring.remote.academicevent.AcademicEventService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AcademicEventModule {
    @Provides
    @Singleton
    fun provideAcademicEventService(@Named("KotlinxSerialization") retrofit: Retrofit): AcademicEventService {
        return retrofit.create()
    }
}
