package com.ku_stacks.ku_ring.di

import android.content.Context
import androidx.room.Room
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideNoticeDatabase(@ApplicationContext context: Context): NoticeDatabase {
        return Room.databaseBuilder(
            context,
            NoticeDatabase::class.java,
            "notices-db")
            .build()
    }

    @Singleton
    @Provides
    fun provideNoticeDao(database: NoticeDatabase): NoticeDao
        = database.noticeDao()
}