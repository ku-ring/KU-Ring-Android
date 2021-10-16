package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.data.api.ITunesClient
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.repository.ITunesRepository
import com.ku_stacks.ku_ring.repository.NoticeRepository
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
    fun provideIRepository(
        itunesClient: ITunesClient
    ): ITunesRepository {
        return ITunesRepository(itunesClient)
    }

    @Provides
    @Singleton
    fun provideNoticeRepository(
        noticeClient: NoticeClient
    ): NoticeRepository {
        return NoticeRepository(noticeClient)
    }
}