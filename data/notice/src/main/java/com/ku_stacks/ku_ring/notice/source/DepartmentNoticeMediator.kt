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
import com.ku_stacks.ku_ring.notice.mapper.toEntityList
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import com.ku_stacks.ku_ring.util.DateUtil

@OptIn(ExperimentalPagingApi::class)
class DepartmentNoticeMediator(
    private val shortName: String,
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

        if(loadType == LoadType.REFRESH) {
            clearNoticePages()
        }

        return try {
            // TODO: 나중에 중요 공지 보여줄 때 주석 해제하기
//            if (page == 0) {
//                loadAndSaveImportantNotices()
//            }
            val noticeResponse = noticeClient.fetchDepartmentNoticeList(
                shortName = shortName,
                page = page,
                size = itemSize
            )

            val startDate = getAppStartedDate()
            val entities = noticeResponse.data.toEntityList(shortName, startDate)
            kuRingDatabase.withTransaction {
                insertNotices(entities, page)
                updateNoticesId(entities)
                updateNoticesCommentCount(entities)
                insertNoticePages(entities, page)
            }

            val isPageEnd = noticeResponse.data.isEmpty()
            MediatorResult.Success(endOfPaginationReached = isPageEnd)
        } catch (e: Exception) {
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

        val startDate = getAppStartedDate()
        val entities = importNoticesResponse.data.toEntityList(shortName, startDate)
        insertNotices(entities, 0)
    }

    private suspend fun insertNotices(noticeEntities: List<NoticeEntity>, page: Int) {
        noticeDao.insertDepartmentNotices(noticeEntities)
    }

    private suspend fun updateNoticesId(entities: List<NoticeEntity>) {
        entities.forEach { entity ->
            if (noticeDao.getDepartmentNoticeId(entity.articleId, entity.department) == 0) {
                noticeDao.updateDepartmentNoticeId(entity.articleId, entity.department, entity.id)
            }
        }
    }

    private suspend fun updateNoticesCommentCount(entities: List<NoticeEntity>) {
        entities.forEach { entity ->
            val noticeId = noticeDao.getDepartmentNoticeId(entity.articleId, entity.department)
            if (noticeId != null) {
                noticeDao.updateDepartmentNoticeCommentCount(
                    entity.articleId, entity.department, entity.commentCount
                )
            }
        }
    }

    private suspend fun insertNoticePages(noticeResponses: List<NoticeEntity>, page: Int) {
        val pageEntities = noticeResponses.map {
            NoticePageEntity(
                articleId = it.articleId,
                category = it.category,
                department = it.department,
                page = page,
            )
        }
        noticePageDao.insertAll(pageEntities)
    }

    private suspend fun clearNoticePages() {
        noticePageDao.clearByDepartment(shortName)
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
                noticePageDao.getNoticePageById(articleId)?.page ?: 0
            }
        }
    }

    private suspend fun getPrependKey(): Int? {
        return noticePageDao.getMinPageByDepartment(shortName)?.minus(1)
    }

    private suspend fun getAppendKey(): Int? {
        return noticePageDao.getMaxPageByDepartment(shortName)?.plus(1)
    }

    companion object {
        const val itemSize = 20
    }
}