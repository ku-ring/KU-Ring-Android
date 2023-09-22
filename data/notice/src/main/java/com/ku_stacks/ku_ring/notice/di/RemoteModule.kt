package com.ku_stacks.ku_ring.notice.di

import com.ku_stacks.ku_ring.notice.api.NoticeClient
import com.ku_stacks.ku_ring.notice.api.NoticeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun provideNoticeService(@Named("Default") retrofit: Retrofit): NoticeService {
        return retrofit.create(NoticeService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoticeClient(noticeService: NoticeService): NoticeClient {
        return NoticeClient(noticeService)
    }
}