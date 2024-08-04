package com.ku_stacks.ku_ring.ai

import com.ku_stacks.ku_ring.ai.repository.KuringBotRepository
import com.ku_stacks.ku_ring.ai.repository.KuringBotRepositoryImpl
import com.ku_stacks.ku_ring.local.room.KuringBotMessageDao
import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException

class KuringBotRepositoryImplTest {
    private val kuringBotClient = Mockito.mock(KuringBotClient::class.java)
    private val dao = Mockito.mock(KuringBotMessageDao::class.java)
    private lateinit var repository: KuringBotRepository

    private val query = "교내,외 장학금 및 학자금 대출 관련 전화번호들을 안내를 해줘"

    @Before
    fun setup() {
        repository = KuringBotRepositoryImpl(kuringBotClient, dao)
    }

    @Test
    fun `test when exception is thrown`() = runTest {
        val tokens = mutableListOf<String>()
        val onData: (String) -> Unit = { data ->
            tokens.add(data)
        }

        // given
        val token = System.currentTimeMillis().toString()
        Mockito.`when`(kuringBotClient.openKuringBotConnection(query, token, onData))
            .thenThrow(MockitoException("test exception"))

        // when
        repository.openKuringBotSession(query, token, onData)

        // then
        assert(tokens.size == 1)
        assert(tokens.contains(KuringBotRepositoryImpl.UNKNOWN_ERROR))
    }
}