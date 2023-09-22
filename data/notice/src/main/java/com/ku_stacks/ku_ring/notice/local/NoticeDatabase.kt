package com.ku_stacks.ku_ring.notice.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoticeEntity::class],
    version = 6,
    exportSchema = false,
)
abstract class NoticeDatabase: RoomDatabase() {
    abstract fun noticeDao(): NoticeDao
}