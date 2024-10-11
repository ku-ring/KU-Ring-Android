package com.ku_stacks.ku_ring.notice.repository

import androidx.paging.PagingData
import com.ku_stacks.ku_ring.domain.Notice
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    /**
     * 공지를 페이징 객체의 형태로 가져온다.
     *
     * @param categoryShortName 가져올 카테고리의 짧은 이름
     * @return 공지 페이징 객체
     */
    fun getNotices(categoryShortName: String): Flow<PagingData<Notice>>
    fun getSavedNotices(): Flow<List<Notice>>
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
