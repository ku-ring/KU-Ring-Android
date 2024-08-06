package com.ku_stacks.ku_ring.kuringbot

import com.ku_stacks.ku_ring.domain.KuringBotMessage
import java.time.LocalDate
import java.time.LocalDateTime


sealed interface KuringBotUIMessage {
    sealed interface Savable {
        val id: Int
        val message: String
        val postedTime: LocalDateTime

        fun toDomain(): KuringBotMessage
    }

    data object Loading : KuringBotUIMessage
    data class QuestionsRemaining(
        val questionsRemaining: Int,
        val postedTime: LocalDate,
    ) : KuringBotUIMessage {
        override fun toString(): String {
            return "질문 가능 횟수 ${questionsRemaining}회 (${postedTime.toKuringBotFormat} 기준)"
        }
    }

    data class Question(
        override val id: Int = 0,
        override val message: String,
        override val postedTime: LocalDateTime,
    ) : KuringBotUIMessage, Savable {
        override fun toDomain(): KuringBotMessage {
            return KuringBotMessage(
                id = id,
                message = message,
                postedDate = postedTime,
                isQuery = true,
            )
        }

        companion object {
            fun create(message: String): Question {
                return Question(
                    message = message,
                    postedTime = LocalDateTime.now(),
                )
            }
        }
    }

    data class Response(
        override val id: Int = 0,
        override val message: String,
        override val postedTime: LocalDateTime,
    ) : KuringBotUIMessage, Savable {
        override fun toDomain(): KuringBotMessage {
            return KuringBotMessage(
                id = id,
                message = message,
                postedDate = postedTime,
                isQuery = false,
            )
        }

        companion object {
            val Empty = Response(
                message = "",
                postedTime = LocalDateTime.now(),
            )
        }
    }
}

private val LocalDate.toKuringBotFormat: String
    get() = "$year. ${monthValue.toPaddedString(2, '0')}. ${dayOfMonth.toPaddedString(2, '0')}"

private fun Int.toPaddedString(length: Int, padChar: Char): String {
    return this.toString().padStart(length, padChar)
}
