package com.ku_stacks.ku_ring.data.db

import androidx.room.Entity

@Entity(tableName = "pageKeys", primaryKeys = ["articleId", "shortName"])
data class PageKeyEntity(
    val articleId: String,
    val shortName: String,
    val page: Int,
)
