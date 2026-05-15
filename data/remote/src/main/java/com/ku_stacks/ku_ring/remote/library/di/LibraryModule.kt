package com.ku_stacks.ku_ring.remote.library.di

import com.ku_stacks.ku_ring.remote.library.LibraryClient
import com.ku_stacks.ku_ring.remote.library.LibraryService
import com.ku_stacks.ku_ring.remote.util.Library
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LibraryModule {
    @Provides
    @Singleton
    fun provideLibraryService(@Library retrofit: Retrofit): LibraryService
        = retrofit.create(LibraryService::class.java)

    @Provides
    @Singleton
    fun provideLibraryClient(libraryService: LibraryService): LibraryClient
        = LibraryClient(libraryService)

}