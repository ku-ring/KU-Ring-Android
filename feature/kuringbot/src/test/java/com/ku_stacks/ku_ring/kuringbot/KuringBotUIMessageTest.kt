package com.ku_stacks.ku_ring.kuringbot

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class KuringBotUIMessageTest {
    @Test
    fun `convert QuestionsRemaining when monthValue is smaller than 10`() {
        assertEquals(
            "질문 가능 횟수 2회 (2024. 08. 10 기준)",
            KuringBotUIMessage.QuestionsRemaining(
                questionsRemaining = 2,
                postedTime = LocalDate.of(2024, 8, 10)
            ).toString()
        )
    }

    @Test
    fun `convert QuestionsRemaining when dayOfMonth is smaller than 10`() {
        assertEquals(
            "질문 가능 횟수 2회 (2024. 10. 03 기준)",
            KuringBotUIMessage.QuestionsRemaining(
                questionsRemaining = 2,
                postedTime = LocalDate.of(2024, 10, 3)
            ).toString()
        )
    }

    @Test
    fun `convert QuestionsRemaining when monthValue and dayOfMonth are smaller than 10`() {
        assertEquals(
            "질문 가능 횟수 2회 (2024. 08. 02 기준)",
            KuringBotUIMessage.QuestionsRemaining(
                questionsRemaining = 2,
                postedTime = LocalDate.of(2024, 8, 2)
            ).toString()
        )
    }
}