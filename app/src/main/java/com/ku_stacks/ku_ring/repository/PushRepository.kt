package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.db.PushDao
import com.ku_stacks.ku_ring.data.db.PushEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PushRepository @Inject constructor(
    private val dao: PushDao
) {
    fun getMyNotification(): Flowable<List<PushEntity>> {
        return dao.getNotification()
            .distinctUntilChanged()
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