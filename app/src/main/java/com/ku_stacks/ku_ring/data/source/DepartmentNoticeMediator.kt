package com.ku_stacks.ku_ring.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.response.DepartmentNoticeResponse
import com.ku_stacks.ku_ring.data.db.KuRingDatabase
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.db.PageKeyEntity
import com.ku_stacks.ku_ring.data.mapper.toEntityList
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.PreferenceUtil
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class DepartmentNoticeMediator(
    private val shortName: String,
    private val noticeClient: NoticeClient,
    private val database: KuRingDatabase,
    private val preferences: PreferenceUtil,
) : RemoteMediator<Int, NoticeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NoticeEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> getRefreshKey(state) ?: 0
            LoadType.PREPEND -> getPrependKey(state)
            LoadType.APPEND -> getAppendKey(state)
        }
        Timber.d("Load dept notices: $shortName, $loadType, $page")

        if (page == null || page < 0) {
            Timber.d("Dept notices skip: $shortName, $loadType, $page")
            return MediatorResult.Success(endOfPaginationReached = page != null)
        }

        return try {
            if (page == 0) {
                loadAndSaveImportantNotices()
            }
            val noticeResponse = noticeClient.fetchDepartmentNoticeList(
                shortName = shortName,
                page = page,
                size = itemSize
            )
            Timber.d("Loaded dept notices: ${noticeResponse.data.lastOrNull()?.articleId}")
            insertNotices(noticeResponse.data, page)

            val isPageEnd = noticeResponse.data.isEmpty()
            if (isPageEnd) {
                Timber.d("Dept notices page end: $shortName, $loadType, $page")
            }
            MediatorResult.Success(endOfPaginationReached = isPageEnd)
        } catch (e: Exception) {
            Timber.e("Dept notices exception: ${e.message}")
            MediatorResult.Error(e)
        }
    }

    private suspend fun loadAndSaveImportantNotices() {
        val importNoticesResponse = noticeClient.fetchDepartmentNoticeList(
            shortName = shortName,
            page = 0,
            size = itemSize,
            important = true
        )
        insertNotices(importNoticesResponse.data, 0)
    }

    private suspend fun insertNotices(notices: List<DepartmentNoticeResponse>, page: Int) {
        val startDate = getAppStartedDate()
        val noticeEntities = notices.toEntityList(shortName, startDate)
        val pageKeyEntities = noticeEntities.map {
            PageKeyEntity(articleId = it.articleId, shortName = shortName, page = page)
        }
        database.withTransaction {
            database.noticeDao().insertDepartmentNotices(noticeEntities)
            database.pageKeyDao().insertPageKeys(pageKeyEntities)
        }
    }

    private fun getAppStartedDate(): String {
        // TODO: NoticeRepositoryImpl.transformRemoteData()에도 있는 이 로직을 PrefUtil로 옮기기
        return preferences.startDate?.takeIf { it.isNotEmpty() } ?: DateUtil.getToday().apply {
            preferences.startDate = this
        }
    }

    private suspend fun getRefreshKey(state: PagingState<Int, NoticeEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.articleId?.let { articleId ->
                database.pageKeyDao().getPageKey(articleId, shortName)?.page
            }
        }
    }

    private suspend fun getPrependKey(state: PagingState<Int, NoticeEntity>): Int? {
        val firstItem = state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
        return firstItem?.let {
            database.pageKeyDao().getPageKey(firstItem.articleId, shortName)?.page?.minus(1)
        }
    }

    private suspend fun getAppendKey(state: PagingState<Int, NoticeEntity>): Int? {
        val lastItem = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
        return lastItem?.let {
            database.pageKeyDao().getPageKey(lastItem.articleId, shortName)?.page?.plus(1)
        }
    }

    companion object {
        const val itemSize = 20
    }
}