package com.ku_stacks.ku_ring.remote.department.di

import com.ku_stacks.ku_ring.remote.department.DepartmentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DepartmentModule {
    @Provides
    @Singleton
    fun provideDepartmentService(@Named("Default") retrofit: Retrofit): DepartmentService {
        return retrofit.create(DepartmentService::class.java)
    }
}