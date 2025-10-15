package com.ku_stacks.ku_ring.notice.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.entity.NoticePageEntity
import com.ku_stacks.ku_ring.local.room.KuRingDatabase
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.local.room.NoticePageDao
import com.ku_stacks.ku_ring.notice.mapper.toEntities
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import com.ku_stacks.ku_ring.remote.notice.response.NoticeResponse
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.util.suspendRunCatching

@OptIn(ExperimentalPagingApi::class)
class CategoryNoticeMediator(
    private val categoryShortName: String,
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val noticePageDao: NoticePageDao,
    private val kuRingDatabase: KuRingDatabase,
    private val preferences: PreferenceUtil,
) : RemoteMediator<Int, NoticeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NoticeEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> getRefreshKey(state) ?: 0
            LoadType.PREPEND -> getPrependKey()
            LoadType.APPEND -> getAppendKey()
        }

        if (page == null || page < 0) {
            return MediatorResult.Success(endOfPaginationReached = page != null)
        }

        if (loadType == LoadType.REFRESH) {
            clearNoticePages()
        }

        return suspendRunCatching {
            val noticeResponse = noticeClient
                .fetchNoticeList(categoryShortName, page, ITEM_SIZE)
                .noticeResponse

            kuRingDatabase.withTransaction {
                insertNotices(noticeResponse)
                insertNoticePages(noticeResponse, page)
            }
            MediatorResult.Success(endOfPaginationReached = noticeResponse.isEmpty())
        }.getOrElse {
            MediatorResult.Error(it)
        }
    }

    private suspend fun insertNotices(noticeResponses: List<NoticeResponse>) {
        val entities = noticeResponses.toEntities(getAppStartedDate())
        noticeDao.insertNotices(entities)
        updateNoticesId(entities)
        updateNoticesCommentCount(entities)
    }

    private suspend fun updateNoticesId(entities: List<NoticeEntity>) {
        entities.forEach { entity ->
            if (noticeDao.getNoticeId(entity.articleId, entity.category) == 0) {
                noticeDao.updateNoticeId(entity.articleId, entity.category, entity.id)
            }
        }
    }

    private suspend fun updateNoticesCommentCount(entities: List<NoticeEntity>) {
        entities.forEach { entity ->
            if (noticeDao.getNoticeId(entity.articleId, entity.category) != null) {
                noticeDao.updateNoticeCommentCount(
                    entity.articleId, entity.category, entity.commentCount
                )
            }
        }
    }

    private suspend fun insertNoticePages(noticeResponses: List<NoticeResponse>, page: Int) {
        val pageEntities = noticeResponses.map {
            NoticePageEntity(
                articleId = it.articleId,
                category = it.category,
                page = page,
            )
        }
        noticePageDao.insertAll(pageEntities)
    }

    private suspend fun clearNoticePages() {
        val category = WordConverter.convertShortNameToFullName(categoryShortName)
        noticePageDao.clearByCategory(category)
    }

    private fun getAppStartedDate(): String {
        return preferences.startDate?.takeIf { it.isNotEmpty() } ?: DateUtil.getToday().apply {
            preferences.startDate = this
        }
    }

    private suspend fun getRefreshKey(state: PagingState<Int, NoticeEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.articleId?.let { articleId ->
                noticePageDao.getNoticePageById(articleId)?.page ?: 0
            }
        }
    }

    private suspend fun getPrependKey(): Int? {
        val category = WordConverter.convertShortNameToFullName(categoryShortName)
        return noticePageDao.getMinPageByCategory(category)?.minus(1)
    }

    private suspend fun getAppendKey(): Int? {
        val category = WordConverter.convertShortNameToFullName(categoryShortName)
        return noticePageDao.getMaxPageByCategory(category)?.plus(1)
    }

    companion object {
        private const val ITEM_SIZE = 20
    }
}