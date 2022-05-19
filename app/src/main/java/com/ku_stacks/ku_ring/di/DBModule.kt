package com.ku_stacks.ku_ring.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ku_stacks.ku_ring.data.db.BlackUserDao
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

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS BlackUserEntity (`userId` TEXT NOT NULL, `nickname` TEXT NOT NULL, `blockedAt` INTEGER NOT NULL, PRIMARY KEY(`userId`))")
        }
    }

    @Singleton
    @Provides
    fun provideKuRingDatabase(@ApplicationContext context: Context): KuRingDatabase {
        return Room.databaseBuilder(
            context,
            KuRingDatabase::class.java,
            "ku-ring-db"
        )
            .addMigrations(MIGRATION_1_2)
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

    @Singleton
    @Provides
    fun provideBlackUserDao(database: KuRingDatabase): BlackUserDao
        = database.blackUserDao()
}