package com.ku_stacks.ku_ring.report.di

import com.ku_stacks.ku_ring.domain.report.ReportRepository
import com.ku_stacks.ku_ring.report.ReportRepositoryImpl
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
    abstract fun bindReportRepository(repositoryImpl: ReportRepositoryImpl): ReportRepository
}