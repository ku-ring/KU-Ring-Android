package com.ku_stacks.ku_ring.staff.di

import com.ku_stacks.ku_ring.staff.remote.StaffService
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
    fun provideSearchService(@Named("Default") retrofit: Retrofit): StaffService {
        return retrofit.create(StaffService::class.java)
    }
}