package com.ku_stacks.ku_ring.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["articleId", "category"])
data class NoticePageEntity(
    @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "department") val department: String? = null,
    @ColumnInfo(name = "page") val page: Int?,
)
