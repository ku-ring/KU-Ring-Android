package com.ku_stacks.ku_ring.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KuringBotMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val message: String,
    val postedEpochSeconds: Long,
    val type: Int,
)

val KuringBotMessageEntity.messageType: KuringBotMessageType
    get() = when (type) {
        0 -> KuringBotMessageType.Query
        1 -> KuringBotMessageType.Response
        2 -> KuringBotMessageType.Error
        else -> KuringBotMessageType.Unknown
    }

enum class KuringBotMessageType {
    Query,
    Response,
    Error,
    Unknown;

    companion object {
        fun from(value: Int): KuringBotMessageType {
            return KuringBotMessageType.entries.firstOrNull { it.ordinal == value } ?: Unknown
        }
    }
}