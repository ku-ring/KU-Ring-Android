package com.ku_stacks.ku_ring.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlackUserEntity(
    @PrimaryKey @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "blockedAt") val blockedAt: Long
)