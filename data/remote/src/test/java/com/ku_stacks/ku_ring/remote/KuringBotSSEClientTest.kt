package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotClient
import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotSSEClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.sse.SSE
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class KuringBotSSEClientTest {
    private val kuringBotClient: KuringBotClient = KuringBotSSEClient(
        HttpClient(CIO) {
            install(SSE) {
                showCommentEvents()
                showRetryEvents()
            }
        }
    )

    private val query = "교내,외 장학금 및 학자금 대출 관련 전화번호들을 안내를 해줘"
    private val wrongQuery = "잘못된 질문"

    @Test
    fun `test session is opened successfully`() = runTest {
        // given
        val testToken = System.currentTimeMillis().toString()

        // when
        val tokens = mutableListOf<String>()
        kuringBotClient.openKuringBotSSESession(query, testToken) {
            tokens.add(it)
        }

        // then
        assertEquals(
            "학생복지처 장학복지팀의 전화번호는 02-450-3211~2이며, 건국사랑/장학사정관장학/기금장학과 관련된 문의는 02-450-3967로 하시면 됩니다.",
            tokens.joinToString("")
        )
    }

    @Test
    fun `get response even when wrong query is given`() = runTest {
        // given
        val testToken = System.currentTimeMillis().toString()

        // when
        val tokens = mutableListOf<String>()
        kuringBotClient.openKuringBotSSESession(wrongQuery, testToken) {
            tokens.add(it)
        }

        // then
        assertEquals("죄송합니다, 관련된 내용에 대하여 알지 못합니다.", tokens.joinToString(""))
    }

    @Test
    fun `get response even when the token is expired`() = runTest {
        // given
        val testToken = System.currentTimeMillis().toString()

        // when
        kuringBotClient.openKuringBotSSESession(query, testToken) {}
        kuringBotClient.openKuringBotSSESession(query, testToken) {}

        val tokens = mutableListOf<String>()
        kuringBotClient.openKuringBotSSESession(query, testToken) {
            tokens.add(it)
        }

        // then
        assertNotNull(tokens)
        assertEquals("남은 질문 횟수가 부족합니다.", tokens.joinToString(""))
    }
}