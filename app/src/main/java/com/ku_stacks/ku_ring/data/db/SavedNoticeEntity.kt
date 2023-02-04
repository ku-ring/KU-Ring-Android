package com.ku_stacks.ku_ring.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedNoticeEntity(
    @PrimaryKey @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "baseUrl") val baseUrl: String,
    @ColumnInfo(name = "postedDate") val postedDate: String,
    @ColumnInfo(name = "subject") val subject: String,
)
