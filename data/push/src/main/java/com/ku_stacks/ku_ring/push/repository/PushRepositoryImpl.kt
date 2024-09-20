package com.ku_stacks.ku_ring.push.repository

import com.ku_stacks.ku_ring.domain.Push
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.push.mapper.toPushList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(
    private val dao: PushDao
) : PushRepository {
    override fun getMyNotificationList(): Flow<List<Push>> {
        return dao.getNotificationList()
            .distinctUntilChanged()
            .map { pushEntityList -> pushEntityList.toPushList() }
    }

    override suspend fun updateNotificationAsOld(articleId: String) {
        dao.updateNotificationAsOld(articleId, false)
    }

    override fun getNotificationCount(): Flow<Int> {
        return dao.getNotificationCount(true)
    }

    override suspend fun deleteNotification(articleId: String) {
        dao.deleteNotification(articleId)
    }

    //not using now
    override suspend fun deleteAllNotification() {
        dao.deleteAllNotification()
    }
}