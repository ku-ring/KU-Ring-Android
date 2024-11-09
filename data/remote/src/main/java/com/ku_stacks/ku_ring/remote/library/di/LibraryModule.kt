package com.ku_stacks.ku_ring.remote.library.di

import com.ku_stacks.ku_ring.remote.library.LibraryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LibraryModule {
    @Provides
    @Singleton
    fun provideLibraryService(@Named("Library") retrofit: Retrofit): LibraryService
        = retrofit.create(LibraryService::class.java)
}