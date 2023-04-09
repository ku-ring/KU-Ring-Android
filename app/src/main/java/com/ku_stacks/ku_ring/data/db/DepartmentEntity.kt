package com.ku_stacks.ku_ring.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey val name: String,
    val shortName: String,
    val koreanName: String,
    val isSubscribed: Boolean,
)
