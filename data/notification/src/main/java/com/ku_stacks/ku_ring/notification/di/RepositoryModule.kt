package com.ku_stacks.ku_ring.notification.di

import com.ku_stacks.ku_ring.notification.repository.NotificationRepository
import com.ku_stacks.ku_ring.notification.repositoryimpl.NotificationRepositoryImpl
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
    abstract fun bindsNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl,
    ): NotificationRepository
}
