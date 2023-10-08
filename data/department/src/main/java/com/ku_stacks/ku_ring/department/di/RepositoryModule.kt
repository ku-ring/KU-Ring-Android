package com.ku_stacks.ku_ring.department.di

import com.ku_stacks.ku_ring.department.repository.DepartmentRepository
import com.ku_stacks.ku_ring.department.repository.DepartmentRepositoryImpl
import com.ku_stacks.ku_ring.local.room.DepartmentDao
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.department.DepartmentClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideDepartmentRepository(
        departmentDao: DepartmentDao,
        departmentClient: DepartmentClient,
        pref: PreferenceUtil,
    ): DepartmentRepository = DepartmentRepositoryImpl(departmentDao, departmentClient, pref)
}