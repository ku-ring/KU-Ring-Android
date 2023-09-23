package com.ku_stacks.ku_ring.notice.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ku_stacks.ku_ring.notice.local.NoticeDao
import com.ku_stacks.ku_ring.notice.local.NoticeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS BlackUserEntity (`userId` TEXT NOT NULL, `nickname` TEXT NOT NULL, `blockedAt` INTEGER NOT NULL, PRIMARY KEY(`userId`))")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE NoticeEntity ADD COLUMN subject TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE NoticeEntity ADD COLUMN postedDate TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE NoticeEntity ADD COLUMN url TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE NoticeEntity ADD COLUMN isSaved INT NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE NoticeEntity ADD COLUMN isReadOnStorage INT NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS departments (" +
                        "name TEXT PRIMARY KEY NOT NULL, " +
                        "shortName TEXT NOT NULL DEFAULT '', " +
                        "koreanName TEXT NOT NULL DEFAULT '', " +
                        "isSubscribed INTEGER NOT NULL DEFAULT 0)"
            )
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE NoticeEntity ADD COLUMN department TEXT NOT NULL DEFAULT ''")
        }
    }

    @Singleton
    @Provides
    fun provideNoticeDatabase(@ApplicationContext context: Context): NoticeDatabase {
        return Room.databaseBuilder(
            context,
            NoticeDatabase::class.java,
            "ku-ring-db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
            .build()
    }

    @Singleton
    @Provides
    fun provideNoticeDao(database: NoticeDatabase): NoticeDao = database.noticeDao()
}