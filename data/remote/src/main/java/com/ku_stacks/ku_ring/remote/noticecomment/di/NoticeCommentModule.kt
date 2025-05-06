package com.ku_stacks.ku_ring.remote.noticecomment.di

import com.ku_stacks.ku_ring.remote.noticecomment.NoticeCommentClient
import com.ku_stacks.ku_ring.remote.noticecomment.NoticeCommentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoticeCommentModule {
    @Provides
    @Singleton
    fun provideNoticeCommentService(@Named("KotlinxSerialization") retrofit: Retrofit): NoticeCommentService {
        return retrofit.create(NoticeCommentService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoticeCommentClient(noticeCommentService: NoticeCommentService): NoticeCommentClient {
        return NoticeCommentClient(noticeCommentService)
    }
}