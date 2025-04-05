package com.ku_stacks.ku_ring.verification.di

import com.ku_stacks.ku_ring.verification.repository.VerificationRepository
import com.ku_stacks.ku_ring.verification.repository.VerificationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindVerificationRepository(
        repositoryImpl: VerificationRepositoryImpl,
    ): VerificationRepository
}
