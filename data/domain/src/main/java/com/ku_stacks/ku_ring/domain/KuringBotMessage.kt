package com.ku_stacks.ku_ring.domain

import java.time.LocalDateTime

data class KuringBotMessage(
    val id: Int,
    val message: String,
    val postedDate: LocalDateTime,
    val type: MessageType,
)

sealed interface MessageType {
    val value: Int

    data object Query : MessageType {
        override val value = 0
    }

    data object Response : MessageType {
        override val value = 1
    }

    data object Error : MessageType {
        override val value = 2
    }

    data object Unknown : MessageType {
        override val value = 3
    }

    companion object {
        fun from(value: Int): MessageType {
            return when (value) {
                0 -> Query
                1 -> Response
                2 -> Error
                3 -> Unknown
                else -> Unknown
            }
        }
    }
}