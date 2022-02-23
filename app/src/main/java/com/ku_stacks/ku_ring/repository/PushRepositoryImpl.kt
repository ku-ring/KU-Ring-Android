package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.db.PushDao
import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.data.mapper.toPushList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(
    private val dao: PushDao
) : PushRepository {
    override fun getMyNotification(): Flowable<List<Push>> {
        return dao.getNotification()
            .distinctUntilChanged()
            .map { pushEntityList -> pushEntityList.toPushList() }
    }

    override fun updateNotification(articleId: String): Completable {
        return dao.updateToReadNotification(articleId, false)
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
            .subscribe({},{})
    }
}