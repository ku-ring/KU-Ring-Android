package com.ku_stacks.ku_ring.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentEntity(
    @PrimaryKey val name: String,
    val shortName: String,
    val koreanName: String,
    val isSubscribed: Boolean,
    val isMainDepartment: Boolean,
)