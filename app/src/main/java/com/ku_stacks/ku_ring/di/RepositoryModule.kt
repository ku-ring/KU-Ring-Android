package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.data.api.ITunesClient
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.db.NoticeDao
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

    //Sample
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
        noticeClient: NoticeClient,
        noticeDao: NoticeDao
    ): NoticeRepository {
        return NoticeRepository(noticeClient, noticeDao)
    }
}