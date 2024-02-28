package com.ku_stacks.ku_ring.user.di

import com.ku_stacks.ku_ring.user.repository.UserRepository
import com.ku_stacks.ku_ring.user.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}