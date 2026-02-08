package com.ku_stacks.ku_ring.club.di

import com.ku_stacks.ku_ring.club.ClubRepositoryImpl
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideClubRepository(clubRepositoryImpl: ClubRepositoryImpl): ClubRepository
}