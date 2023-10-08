package com.ku_stacks.ku_ring.push.repository

import com.ku_stacks.ku_ring.domain.Push
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface PushRepository {
    fun getMyNotificationList(): Flowable<List<Push>>
    fun updateNotificationAsOld(articleId: String): Completable
    fun getNotificationCount(): Flowable<Int>
    fun deleteNotification(articleId: String)
    fun deleteAllNotification()
}