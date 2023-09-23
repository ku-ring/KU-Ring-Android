package com.ku_stacks.ku_ring.department.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DepartmentEntity::class],
    version = 6,
    exportSchema = false,
)
abstract class DepartmentDatabase: RoomDatabase() {
    abstract fun departmentDao(): DepartmentDao
}