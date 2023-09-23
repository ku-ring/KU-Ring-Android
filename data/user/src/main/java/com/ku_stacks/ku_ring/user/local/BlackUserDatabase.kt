package com.ku_stacks.ku_ring.user.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BlackUserEntity::class],
    version = 6,
    exportSchema = false,
)
abstract class BlackUserDatabase: RoomDatabase() {
    abstract fun blackUserDao(): BlackUserDao
}