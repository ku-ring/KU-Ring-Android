package com.ku_stacks.ku_ring.work.scheduler

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationWork

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkSchedulerModule {
    @Binds
    @Singleton
    @ApplicationWork
    abstract fun bindApplicationWorkScheduler(
        applicationWorkScheduler: ApplicationWorkScheduler,
    ): WorkScheduler
}