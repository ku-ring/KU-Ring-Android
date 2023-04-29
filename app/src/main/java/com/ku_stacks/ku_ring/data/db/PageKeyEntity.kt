package com.ku_stacks.ku_ring.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pageKeys")
data class PageKeyEntity(
    @PrimaryKey val articleId: String,
    val page: Int,
)
