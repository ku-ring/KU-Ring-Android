package com.ku_stacks.ku_ring.push.repository

import com.ku_stacks.ku_ring.domain.Push
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.push.mapper.toPushList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(
    private val dao: PushDao
) : PushRepository {
    override fun getMyNotificationList(): Flowable<List<Push>> {
        return dao.getNotificationList()
            .distinctUntilChanged()
            .map { pushEntityList -> pushEntityList.toPushList() }
    }

    override fun updateNotificationAsOld(articleId: String): Completable {
        return dao.updateNotificationAsOld(articleId, false)
    }

    override fun getNotificationCount(): Flowable<Int> {
        return dao.getNotificationCount(true)
    }

    override fun deleteNotification(articleId: String) {
        dao.deleteNotification(articleId)
            .subscribeOn(Schedulers.io())
            .subscribe({}, {})
    }

    //not using now
    override fun deleteAllNotification() {
        dao.deleteAllNotification()
            .subscribeOn(Schedulers.io())
            .subscribe({}, {})
    }
}