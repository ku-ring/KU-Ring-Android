package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.model.Push
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface PushRepository {
    fun getMyNotification(): Flowable<List<Push>>
    fun updateNotification(articleId: String): Completable
    fun getNotificationCount(): Flowable<Int>
    fun deleteNotification(articleId: String)
    fun deleteAllNotification()
}