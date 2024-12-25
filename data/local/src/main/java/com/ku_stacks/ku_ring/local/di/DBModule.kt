package com.ku_stacks.ku_ring.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ku_stacks.ku_ring.local.room.KuRingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    private val MIGRATION_1_2 =
        object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS BlackUserEntity (`userId` TEXT NOT NULL, `nickname` TEXT NOT NULL, `blockedAt` INTEGER NOT NULL, PRIMARY KEY(`userId`))",
                )
            }
        }

    private val MIGRATION_2_3 =
        object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE NoticeEntity ADD COLUMN subject TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE NoticeEntity ADD COLUMN postedDate TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE NoticeEntity ADD COLUMN url TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE NoticeEntity ADD COLUMN isSaved INT NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE NoticeEntity ADD COLUMN isReadOnStorage INT NOT NULL DEFAULT 0")
            }
        }

    private val MIGRATION_3_4 =
        object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS departments (" +
                        "name TEXT PRIMARY KEY NOT NULL, " +
                        "shortName TEXT NOT NULL DEFAULT '', " +
                        "koreanName TEXT NOT NULL DEFAULT '', " +
                        "isSubscribed INTEGER NOT NULL DEFAULT 0)",
                )
            }
        }

    private val MIGRATION_4_5 =
        object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE NoticeEntity ADD COLUMN department TEXT NOT NULL DEFAULT ''")
            }
        }

    private val MIGRATION_5_6 =
        object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE NoticeEntity ADD COLUMN isImportant INT NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE departments ADD COLUMN isMainDepartment INT NOT NULL DEFAULT 0")
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `SearchHistoryEntity` " +
                        "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`keyword` TEXT NOT NULL)",
                )
            }
        }

    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS KuringBotMessageEntity(
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    message TEXT NOT NULL,
                    postedEpochSeconds INTEGER NOT NULL,
                    isQuery INTEGER NOT NULL
                )
            """.trimIndent()
            )
        }
    }

    private val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS CategoryOrderEntity(
                    koreanName TEXT PRIMARY KEY NOT NULL,
                    shortName TEXT NOT NULL,
                    categoryOrder INTEGER NOT NULL
                )
            """.trimIndent()
            )
            db.execSQL(
                """
                INSERT INTO CategoryOrderEntity (koreanName, shortName, categoryOrder)
                VALUES
                    ('학과', 'dep', 0),
                    ('학사', 'bch', 1),
                    ('장학', 'sch', 2),
                    ('도서관', 'lib', 3),
                    ('취창업', 'emp', 4),
                    ('국제', 'nat', 5),
                    ('학생', 'stu', 6),
                    ('산학', 'ind', 7),
                    ('일반', 'nor', 8);
            """.trimIndent()
            )
        }
    }

    @Singleton
    @Provides
    fun provideKuRingDatabase(
        @ApplicationContext context: Context,
    ): KuRingDatabase =
        Room
            .databaseBuilder(
                context,
                KuRingDatabase::class.java,
                "ku-ring-db",
            ).addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3,
                MIGRATION_3_4,
                MIGRATION_4_5,
                MIGRATION_5_6,
                MIGRATION_6_7,
                MIGRATION_7_8,
            ).build()

    @Singleton
    @Provides
    fun provideNoticeDao(database: KuRingDatabase) = database.noticeDao()

    @Singleton
    @Provides
    fun providePushDao(database: KuRingDatabase) = database.pushDao()

    @Singleton
    @Provides
    fun provideBlackUserDao(database: KuRingDatabase) = database.blackUserDao()

    @Singleton
    @Provides
    fun provideDepartmentDao(database: KuRingDatabase) = database.departmentDao()

    @Singleton
    @Provides
    fun provideSearchHistoryDao(database: KuRingDatabase) = database.searchHistoryDao()

    @Singleton
    @Provides
    fun provideKuringBotMessageDao(database: KuRingDatabase) = database.kuringBotMessageDao()

    @Singleton
    @Provides
    fun provideCategoryOrderDao(database: KuRingDatabase) = database.categoryOrderDao()
}
