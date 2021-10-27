package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.db.PushDao
import com.ku_stacks.ku_ring.data.db.PushEntity
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class PushRepository @Inject constructor(
    private val dao: PushDao
) {
    fun getMyNotification(): Flowable<List<PushEntity>> {
        return dao.getNotification()
            .distinctUntilChanged()
    }
}