package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.db.PushDao
import com.ku_stacks.ku_ring.data.entity.Push
import com.ku_stacks.ku_ring.data.mapper.transformPush
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PushRepository @Inject constructor(
    private val dao: PushDao
) {
    fun getMyNotification(): Flowable<List<Push>> {
        return dao.getNotification()
            .distinctUntilChanged()
            .map { transformPush(it) }
    }

    fun updateNotification(articleId: String): Completable {
        return dao.updateConfirmedNotification(articleId, false)
    }

    fun getNotificationCount(): Flowable<Int> {
        return dao.getNotificationCount(true)
    }

    //not using now
    fun deleteAllNotification() {
        dao.deleteAllNotification()
            .subscribeOn(Schedulers.io())
            .subscribe({},{})
    }
}