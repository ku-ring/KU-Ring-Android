package com.ku_stacks.ku_ring.notice.di

import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.api.NoticeClient
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.notice.repository.NoticeRepositoryImpl
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.IODispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNoticeRepository(
        noticeClient: NoticeClient,
        noticeDao: NoticeDao,
        pref: PreferenceUtil,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): NoticeRepository {
        return NoticeRepositoryImpl(noticeClient, noticeDao, pref, ioDispatcher)
    }
}