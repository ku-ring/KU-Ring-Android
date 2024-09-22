package com.ku_stacks.ku_ring.ai.repository

import com.ku_stacks.ku_ring.ai.mapper.toDomain
import com.ku_stacks.ku_ring.ai.mapper.toEntity
import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.local.room.KuringBotMessageDao
import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotClient
import com.ku_stacks.ku_ring.remote.user.UserClient
import com.ku_stacks.ku_ring.util.toEpochSecond
import java.time.LocalDate
import javax.inject.Inject

class KuringBotRepositoryImpl @Inject constructor(
    private val kuringBotClient: KuringBotClient,
    private val kuringBotMessageDao: KuringBotMessageDao,
    private val userClient: UserClient,
) : KuringBotRepository {

    override suspend fun openKuringBotSession(
        query: String,
        token: String,
        onReceived: (String) -> Unit,
    ) {
        try {
            kuringBotClient.openKuringBotConnection(query, token, onReceived)
        } catch (e: Exception) {
            onReceived(UNKNOWN_ERROR)
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

    override suspend fun getQueryCount(from: LocalDate, to: LocalDate): Int {
        return kuringBotMessageDao.getQueryCount(from.toEpochSecond(), to.toEpochSecond())
    }

    override suspend fun getQueryCount(token: String): Int {
        return userClient.getKuringBotQueryCount(token)
    }

    companion object {
        internal const val UNKNOWN_ERROR = "예상치 못한 오류가 발생했어요. 다음에 다시 시도해 주세요."
    }
}