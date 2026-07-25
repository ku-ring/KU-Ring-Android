package com.ku_stacks.ku_ring.remote.place.di

import com.ku_stacks.ku_ring.remote.place.PlaceClient
import com.ku_stacks.ku_ring.remote.place.PlaceService
import com.ku_stacks.ku_ring.remote.util.Default
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceModule {
    @Provides
    @Singleton
    fun providePlaceService(@Default retrofit: Retrofit): PlaceService =
        retrofit.create(PlaceService::class.java)

    @Provides
    @Singleton
    fun providePlaceClient(placeService: PlaceService): PlaceClient =
        PlaceClient(placeService)
}
