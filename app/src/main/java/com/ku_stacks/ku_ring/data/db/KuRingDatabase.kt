package com.ku_stacks.ku_ring.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ku_stacks.ku_ring.notice.local.NoticeDao
import com.ku_stacks.ku_ring.notice.local.NoticeEntity

@Database(
    entities = [
        NoticeEntity::class,
        PushEntity::class,
        BlackUserEntity::class,
        DepartmentEntity::class,
    ],
    version = 5,
    exportSchema = false
)
abstract class KuRingDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao
    abstract fun pushDao(): PushDao
    abstract fun blackUserDao(): BlackUserDao
    abstract fun departmentDao(): DepartmentDao
}