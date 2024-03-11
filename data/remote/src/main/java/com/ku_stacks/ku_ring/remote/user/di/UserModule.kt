package com.ku_stacks.ku_ring.remote.user.di

import com.ku_stacks.ku_ring.remote.user.UserClient
import com.ku_stacks.ku_ring.remote.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideFeedbackService(@Named("Default") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedbackClient(userService: UserService): UserClient {
        return UserClient(userService)
    }
}