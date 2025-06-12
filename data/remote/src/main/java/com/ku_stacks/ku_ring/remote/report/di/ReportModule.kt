package com.ku_stacks.ku_ring.remote.report.di

import com.ku_stacks.ku_ring.remote.report.ReportClient
import com.ku_stacks.ku_ring.remote.report.ReportService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportModule {
    @Provides
    @Singleton
    fun provideReportService(@Named("KotlinxSerialization") retrofit: Retrofit): ReportService {
        return retrofit.create(ReportService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportClient(reportService: ReportService): ReportClient {
        return ReportClient(reportService)
    }
}