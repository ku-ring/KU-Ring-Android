package com.ku_stacks.ku_ring.kuringbot

import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.kuringbot.mapper.toUIMessage
import com.ku_stacks.ku_ring.util.yearMonth
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class KuringBotMessageCounter @Inject constructor() {

    private val messageCount = mutableMapOf<YearMonth, Int>()

    fun convertInitialUIMessages(messages: List<KuringBotMessage>): List<KuringBotUIMessage> {
        val uiMessages = mutableListOf<KuringBotUIMessage>()

        messages.forEachIndexed { index, message ->
            uiMessages.add(message.toUIMessage())
            if (message.isQuery) {
                increaseMessageCount(message)
            }
            // 답변이거나
            // 질문이면서 다음 메시지가 답변이 아닌 경우 (즉 답변이 오기 전에 중단된 경우)
            if (!message.isQuery || !(index + 1 in messages.indices && !messages[index + 1].isQuery)) {
                uiMessages.add(calculateQuestionsRemaining(message.postedDate))
            }
        }

        return uiMessages
    }

    fun increaseMessageCount(message: KuringBotMessage) {
        messageCount[message.yearMonth] = messageCount.getOrDefault(message.yearMonth, 0) + 1
    }

    fun calculateQuestionsRemaining(dateTime: LocalDateTime): KuringBotUIMessage.QuestionsRemaining {
        val questionsCount = messageCount.getOrDefault(dateTime.yearMonth, 0)
        return KuringBotUIMessage.QuestionsRemaining(
            (MONTHLY_LIMIT - questionsCount).coerceAtLeast(0),
            dateTime.toLocalDate(),
        )
    }

    private val KuringBotMessage.yearMonth: YearMonth
        get() = YearMonth.of(postedDate.year, postedDate.monthValue)

    companion object {
        private const val MONTHLY_LIMIT = 2
    }
}