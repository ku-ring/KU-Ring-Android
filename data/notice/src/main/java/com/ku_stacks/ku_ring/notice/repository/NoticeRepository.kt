package com.ku_stacks.ku_ring.notice.repository

import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.api.request.SubscribeRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNotices(type: String, scope: CoroutineScope): Flowable<PagingData<Notice>>
    fun getSavedNotices(): Flow<List<Notice>>
    fun getSavedNoticeList(): List<Notice>
    fun insertNoticeAsOld(notice: Notice): Completable
    fun updateNoticeToBeRead(articleId: String, category: String): Completable
    suspend fun updateSavedStatus(articleId: String, category: String, isSaved: Boolean)
    suspend fun updateNoticeToBeReadOnStorage(articleId: String, category: String)
    suspend fun clearSavedNotices()
    fun deleteAllNoticeRecord()
    fun deleteSharedPreference()
    fun getDepartmentNotices(shortName: String): Flow<PagingData<Notice>>
    fun fetchSubscriptionFromRemote(token: String): Single<List<String>>
    fun saveSubscriptionToRemote(token: String, subscribeRequest: SubscribeRequest)
}