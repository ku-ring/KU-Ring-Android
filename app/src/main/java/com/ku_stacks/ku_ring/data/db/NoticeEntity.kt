package com.ku_stacks.ku_ring.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoticeEntity(
    @PrimaryKey @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "isRead") val isRead: Boolean,
    @ColumnInfo(name = "isUpdate") val isUpdate: Boolean
)