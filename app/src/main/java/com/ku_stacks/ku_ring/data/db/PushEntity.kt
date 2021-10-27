package com.ku_stacks.ku_ring.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PushEntity(
    @PrimaryKey
    @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isNew") val isNew: Boolean,
)