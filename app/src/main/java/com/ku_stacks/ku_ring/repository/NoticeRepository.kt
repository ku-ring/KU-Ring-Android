package com.ku_stacks.ku_ring.repository

import androidx.paging.PagingData
import com.ku_stacks.ku_ring.data.model.Notice
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNotices(type: String, scope: CoroutineScope): Flowable<PagingData<Notice>>
    fun getSavedNotices(): Flow<List<Notice>>
    fun getSavedNoticeList(): List<Notice>
    fun insertNoticeAsOld(notice: Notice): Completable
    fun updateNoticeToBeRead(articleId: String): Completable
    suspend fun updateSavedStatus(articleId: String, isSaved: Boolean)
    suspend fun updateNoticeToBeReadOnStorage(articleId: String)
    suspend fun clearSavedNotices()
    fun deleteAllNoticeRecord()
    fun deleteSharedPreference()
}