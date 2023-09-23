package com.ku_stacks.ku_ring.push.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PushEntity::class],
    version = 6,
    exportSchema = false,
)
abstract class PushDatabase : RoomDatabase() {
    abstract fun pushDao(): PushDao
}