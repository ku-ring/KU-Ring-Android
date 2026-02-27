package com.ku_stacks.ku_ring.notification.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ku_stacks.ku_ring.local.entity.PushEntity
import com.ku_stacks.ku_ring.local.room.PushDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationDataSource @Inject constructor(
    private val pushDao: PushDao,
) {
    fun getNotificationPager(pageSize: Int): Pager<Int, PushEntity> =
        Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { pushDao.getNotificationList() },
        )

    suspend fun updateNotificationAsRead(id: Int, isNew: Boolean) {
        pushDao.updateNotificationAsOld(id, isNew)
    }

    suspend fun deleteNotification(id: Int) {
        pushDao.deleteNotification(id)
    }
}
