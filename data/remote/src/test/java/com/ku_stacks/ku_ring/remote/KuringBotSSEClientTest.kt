package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotClient
import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotSSEClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.sse.SSE
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
        kuringBotClient.openKuringBotConnection(query, testToken) {
            tokens.add(it)
        }

        // then
        assert(tokens.isNotEmpty())
    }

    @Test
    fun `get response even when wrong query is given`() = runTest {
        // given
        val testToken = System.currentTimeMillis().toString()

        // when
        val tokens = mutableListOf<String>()
        kuringBotClient.openKuringBotConnection(wrongQuery, testToken) {
            tokens.add(it)
        }

        // then
        assert(tokens.isNotEmpty())
    }

    @Test
    fun `get response even when the token is expired`() = runTest {
        // given
        val testToken = System.currentTimeMillis().toString()

        // when
        kuringBotClient.openKuringBotConnection(query, testToken) {}
        kuringBotClient.openKuringBotConnection(query, testToken) {}

        val tokens = mutableListOf<String>()
        kuringBotClient.openKuringBotConnection(query, testToken) {
            tokens.add(it)
        }

        // then
        assert(tokens.isNotEmpty())
    }
}