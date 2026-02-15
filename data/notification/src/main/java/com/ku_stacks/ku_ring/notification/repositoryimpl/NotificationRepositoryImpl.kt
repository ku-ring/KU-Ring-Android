package com.ku_stacks.ku_ring.notification.repositoryimpl

import androidx.paging.PagingData
import androidx.paging.map
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.notification.datasource.NotificationDataSource
import com.ku_stacks.ku_ring.notification.mapper.toDomain
import com.ku_stacks.ku_ring.notification.repository.NotificationRepository
import com.ku_stacks.ku_ring.util.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
    @field:IODispatcher private val ioDispatcher: CoroutineDispatcher
) : NotificationRepository {
    override fun getNotificationList(): Flow<PagingData<Notification>> =
        notificationDataSource.getNotificationPager(pageSize = PAGE_SIZE)
            .flow.map { pagingData ->
                pagingData.map { it.toDomain() }
            }

    override suspend fun updateNotificationAsRead(articleId: String) =
        withContext(ioDispatcher) {
            notificationDataSource.updateNotificationAsRead(articleId, true)
        }

    override suspend fun deleteNotification(articleId: String) =
        withContext(ioDispatcher) {
            notificationDataSource.deleteNotification(articleId)
        }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
