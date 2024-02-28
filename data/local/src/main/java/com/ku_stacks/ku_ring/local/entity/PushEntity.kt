package com.ku_stacks.ku_ring.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PushEntity(
    @PrimaryKey
    @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "postedDate") val postedDate: String,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "baseUrl") val fullUrl: String,
    @ColumnInfo(name = "isNew") val isNew: Boolean,
    @ColumnInfo(name = "receivedDate") val receivedDate: String
)