package com.ku_stacks.ku_ring.di

import android.content.Context
import androidx.room.Room
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.KuRingDatabase
import com.ku_stacks.ku_ring.data.db.PushDao
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
    fun provideKuRingDatabase(@ApplicationContext context: Context): KuRingDatabase {
        return Room.databaseBuilder(
            context,
            KuRingDatabase::class.java,
            "ku-ring-db")
            .build()
    }

    @Singleton
    @Provides
    fun provideNoticeDao(database: KuRingDatabase): NoticeDao
        = database.noticeDao()

    @Singleton
    @Provides
    fun providePushDao(database: KuRingDatabase): PushDao
        = database.pushDao()
}