package com.ku_stacks.ku_ring.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryOrderEntity(
    @PrimaryKey val koreanName: String,
    val shortName: String,
    @ColumnInfo(name = "categoryOrder") val order: Int,
)