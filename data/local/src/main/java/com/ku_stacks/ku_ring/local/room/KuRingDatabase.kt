package com.ku_stacks.ku_ring.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ku_stacks.ku_ring.local.entity.BlackUserEntity
import com.ku_stacks.ku_ring.local.entity.DepartmentEntity
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.entity.PushEntity
import com.ku_stacks.ku_ring.local.entity.SearchHistoryEntity

@Database(
    entities = [
        NoticeEntity::class,
        PushEntity::class,
        BlackUserEntity::class,
        DepartmentEntity::class,
        SearchHistoryEntity::class,
    ],
    version = 7,
    exportSchema = false
)
abstract class KuRingDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao
    abstract fun pushDao(): PushDao
    abstract fun blackUserDao(): BlackUserDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}
