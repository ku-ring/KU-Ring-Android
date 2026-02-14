package com.ku_stacks.ku_ring.notification.repository

import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotificationList(): Flow<PagingData<Notification>>
    suspend fun updateNotificationAsRead(articleId: String)
    suspend fun deleteNotification(articleId: String)
}
