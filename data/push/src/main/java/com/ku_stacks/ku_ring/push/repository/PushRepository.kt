package com.ku_stacks.ku_ring.push.repository

import com.ku_stacks.ku_ring.domain.Push
import kotlinx.coroutines.flow.Flow

interface PushRepository {
    fun getMyNotificationList(): Flow<List<Push>>
    suspend fun updateNotificationAsOld(articleId: String)
    fun getNotificationCount(): Flow<Int>
    suspend fun deleteNotification(articleId: String)
    suspend fun deleteAllNotification()
}