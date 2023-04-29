package com.ku_stacks.ku_ring.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        NoticeEntity::class,
        PushEntity::class,
        BlackUserEntity::class,
        DepartmentEntity::class,
        PageKeyEntity::class,
    ],
    version = 5,
    exportSchema = false
)
abstract class KuRingDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao
    abstract fun pushDao(): PushDao
    abstract fun blackUserDao(): BlackUserDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun pageKeyDao(): PageKeyDao
}