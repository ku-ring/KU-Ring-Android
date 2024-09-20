package com.ku_stacks.ku_ring.notice.repository

import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNotices(type: String, scope: CoroutineScope): Flow<PagingData<Notice>>
    fun getSavedNotices(): Flow<List<Notice>>
    suspend fun insertNotice(notice: Notice)
    suspend fun insertNoticeAsOld(notice: Notice)
    suspend fun updateNoticeToBeRead(articleId: String, category: String)
    suspend fun updateSavedStatus(articleId: String, category: String, isSaved: Boolean)
    suspend fun updateNoticeToBeReadOnStorage(articleId: String, category: String)
    suspend fun clearSavedNotices()
    suspend fun deleteAllNoticeRecord()
    suspend fun deleteSharedPreference()
    fun getDepartmentNotices(shortName: String): Flow<PagingData<Notice>>
    suspend fun fetchSubscriptionFromRemote(token: String): List<String>
    suspend fun saveSubscriptionToRemote(token: String, subscribeCategories: List<String>)
    suspend fun getNoticeSearchResult(query: String): List<Notice>
}
