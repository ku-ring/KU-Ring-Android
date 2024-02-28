package com.ku_stacks.ku_ring.staff.di

import com.ku_stacks.ku_ring.staff.repository.StaffRepository
import com.ku_stacks.ku_ring.staff.repository.StaffRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindStaffRepository(repositoryImpl: StaffRepositoryImpl): StaffRepository
}