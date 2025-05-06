package com.ku_stacks.ku_ring.noticecomment.di

import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository
import com.ku_stacks.ku_ring.noticecomment.NoticeCommentRepositoryImpl
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
    abstract fun provideNoticeCommentRepository(repositoryImpl: NoticeCommentRepositoryImpl): NoticeCommentRepository
}