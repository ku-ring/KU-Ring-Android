package com.ku_stacks.ku_ring.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KuringBotMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val message: String,
    val postedEpochSeconds: Long,
    val isQuery: Boolean,
)