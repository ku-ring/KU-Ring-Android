package com.ku_stacks.ku_ring.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoticePageEntity(
    @PrimaryKey @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo("category") val category: String,
    @ColumnInfo("department") val department: String? = null,
    @ColumnInfo(name = "page") val page: Int?,
)
