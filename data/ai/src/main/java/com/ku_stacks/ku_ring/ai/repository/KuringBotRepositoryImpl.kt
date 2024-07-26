package com.ku_stacks.ku_ring.ai.repository

import com.ku_stacks.ku_ring.ai.isSSEData
import com.ku_stacks.ku_ring.ai.mapper.toDomain
import com.ku_stacks.ku_ring.ai.mapper.toEntity
import com.ku_stacks.ku_ring.ai.parseSSEData
import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.local.room.KuringBotMessageDao
import com.ku_stacks.ku_ring.remote.sse.SSEDataSource
import javax.inject.Inject

class KuringBotRepositoryImpl @Inject constructor(
    private val sseDataSource: SSEDataSource,
    private val kuringBotMessageDao: KuringBotMessageDao,
) : KuringBotRepository {

    override suspend fun openKuringBotSession(
        query: String,
        token: String,
        onSSEData: (String) -> Unit,
        onOtherData: (String) -> Unit
    ) {
        sseDataSource.openKuringBotSSESession(query, token) {
            if (it.isSSEData()) {
                onSSEData(it.parseSSEData())
            } else {
                onOtherData(it)
            }
        }
    }

    override suspend fun getAllMessages(): List<KuringBotMessage> {
        return kuringBotMessageDao.getAllMessages().toDomain()
    }

    override suspend fun insertMessages(messages: List<KuringBotMessage>) {
        kuringBotMessageDao.insertMessages(messages.toEntity())
    }

    override suspend fun insertMessage(message: KuringBotMessage) {
        kuringBotMessageDao.insertMessage(message.toEntity())
    }

    override suspend fun clear() {
        kuringBotMessageDao.clear()
    }
}