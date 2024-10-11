package com.ku_stacks.ku_ring.notice.repository

import androidx.paging.*
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.mapper.toNotice
import com.ku_stacks.ku_ring.notice.mapper.toNoticeList
import com.ku_stacks.ku_ring.notice.source.CategoryNoticeMediator
import com.ku_stacks.ku_ring.notice.source.DepartmentNoticeMediator
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.util.IODispatcher
import com.ku_stacks.ku_ring.util.WordConverter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val pref: PreferenceUtil,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : NoticeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNotices(categoryShortName: String): Flow<PagingData<Notice>> {
        val pagingSourceFactory = {
            noticeDao.getNotices(WordConverter.convertShortNameToFullName(categoryShortName))
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = CategoryNoticeMediator(
                categoryShortName,
                noticeClient,
                noticeDao,
                pref,
            ),
            pagingSourceFactory = pagingSourceFactory,
        ).flow.map { noticeEntityPagingData -> noticeEntityPagingData.map { it.toNotice() } }
    }

    override fun getSavedNotices(): Flow<List<Notice>> {
        return noticeDao.getNoticesBySaved(true).map {
            it.toNoticeList().sortedByDescending { notice -> notice.postedDate }
        }
    }

    override suspend fun updateNoticeToBeRead(
        articleId: String,
        category: String
    ) {
        withContext(ioDispatcher) {
            noticeDao.updateNoticeAsRead(
                articleId,
                category
            )
        }
    }

    override suspend fun updateSavedStatus(
        articleId: String,
        category: String,
        isSaved: Boolean
    ) {
        withContext(ioDispatcher) {
            noticeDao.updateNoticeSaveState(
                articleId,
                category,
                isSaved
            )
            if (!isSaved) {
                noticeDao.updateNoticeAsReadOnStorage(
                    articleId,
                    category,
                    false
                )
            }
        }
    }

    override suspend fun updateNoticeToBeReadOnStorage(
        articleId: String,
        category: String
    ) {
        withContext(ioDispatcher) {
            noticeDao.updateNoticeAsReadOnStorage(
                articleId,
                category,
                true
            )
        }
    }

    override suspend fun clearSavedNotices() {
        withContext(ioDispatcher) {
            noticeDao.clearSavedNotices()
        }
    }

    override suspend fun deleteAllNoticeRecord() { // for testing
        noticeDao.deleteAllNoticeRecord()
    }

    override suspend fun deleteSharedPreference() { //for testing
        pref.deleteStartDate()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getDepartmentNotices(shortName: String): Flow<PagingData<Notice>> {
        val pagingSourceFactory = { noticeDao.getDepartmentNotices(shortName) }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
            ),
            remoteMediator = DepartmentNoticeMediator(
                shortName,
                noticeClient,
                noticeDao,
                pref,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { noticeEntityPagingData -> noticeEntityPagingData.map { it.toNotice() } }
    }

    override suspend fun fetchSubscriptionFromRemote(token: String): List<String> {
        return noticeClient.fetchSubscribe(token).categoryList.map { it.koreanName }
    }

    override suspend fun saveSubscriptionToRemote(
        token: String,
        subscribeCategories: List<String>
    ) {
        val subscribeRequest = SubscribeRequest(subscribeCategories)
        runCatching {
            noticeClient.saveSubscribe(
                token,
                subscribeRequest
            )
        }.onSuccess {
            if (it.isSuccess) {
                pref.firstRunFlag = false
            }
        }
    }

    override suspend fun getNoticeSearchResult(query: String): List<Notice> =
        withContext(Dispatchers.IO) {
            val result = noticeClient.fetchNoticeList(query).takeIf { it.isSuccess }?.toNoticeList()
                ?: emptyList()

            val savedArticleIdSet = noticeDao.getSavedNoticeList(true).map { it.articleId }.toSet()

            result.map {
                it.copy(isSaved = savedArticleIdSet.contains(it.articleId))
            }
        }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
