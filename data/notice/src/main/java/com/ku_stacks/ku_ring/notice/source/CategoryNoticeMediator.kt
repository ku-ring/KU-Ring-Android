package com.ku_stacks.ku_ring.notice.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.mapper.toEntities
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import com.ku_stacks.ku_ring.remote.notice.response.NoticeResponse
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.suspendRunCatching

@OptIn(ExperimentalPagingApi::class)
class CategoryNoticeMediator(
    private val categoryShortName: String,
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val preferences: PreferenceUtil,
) : RemoteMediator<Int, NoticeEntity>() {

    private val pageNumberMap = hashMapOf<CategoryNoticePageKey, Int>()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NoticeEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> getRefreshKey(state) ?: 0
            LoadType.PREPEND -> getPrependKey(state)
            LoadType.APPEND -> getAppendKey(state)
        }

        if (page == null || page < 0) {
            return MediatorResult.Success(endOfPaginationReached = page != null)
        }

        return suspendRunCatching {
            val noticeResponse = noticeClient.fetchNoticeList(categoryShortName, page, itemSize)
            insertNotices(noticeResponse.noticeResponse)

            MediatorResult.Success(endOfPaginationReached = noticeResponse.noticeResponse.isEmpty())
        }.getOrElse {
            MediatorResult.Error(it)
        }
    }

    private suspend fun insertNotices(noticeResponses: List<NoticeResponse>) {
        val entities = noticeResponses.toEntities(getAppStartedDate())
        noticeDao.insertNotices(entities)
        updateNoticesId(entities)
    }

    private suspend fun updateNoticesId(entities: List<NoticeEntity>) {
        entities.forEach { entity ->
            if (noticeDao.getNoticeId(entity.articleId, entity.category) == 0) {
                noticeDao.updateNoticeId(entity.articleId, entity.category, entity.id)
            }
        }
    }

    private fun getAppStartedDate(): String {
        return preferences.startDate?.takeIf { it.isNotEmpty() } ?: DateUtil.getToday().apply {
            preferences.startDate = this
        }
    }

    private fun getRefreshKey(state: PagingState<Int, NoticeEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.articleId?.let { articleId ->
                pageNumberMap[CategoryNoticePageKey(articleId, categoryShortName)]
            }
        }
    }

    private fun getPrependKey(state: PagingState<Int, NoticeEntity>): Int? {
        val firstItem = state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
        return firstItem?.let {
            pageNumberMap[CategoryNoticePageKey(it.articleId, categoryShortName)]?.minus(1)
        }
    }

    private fun getAppendKey(state: PagingState<Int, NoticeEntity>): Int? {
        val lastItem = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
        return lastItem?.let {
            // page number가 존재하지 않는 공지: 이전 실행에서 로드된 공지
            // 따라서 page size를 통해 간접적으로 page key를 계산해야 한다.
            (pageNumberMap[CategoryNoticePageKey(it.articleId, categoryShortName)]
                ?: state.pages.size).plus(1)
        }
    }

    companion object {
        private const val itemSize = 20
    }
}

private data class CategoryNoticePageKey(
    val articleId: String,
    val categoryShortName: String,
)