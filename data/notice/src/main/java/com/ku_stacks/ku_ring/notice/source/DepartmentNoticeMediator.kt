package com.ku_stacks.ku_ring.notice.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.mapper.toEntityList
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import com.ku_stacks.ku_ring.util.DateUtil

@OptIn(ExperimentalPagingApi::class)
class DepartmentNoticeMediator(
    private val shortName: String,
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val preferences: PreferenceUtil,
) : RemoteMediator<Int, NoticeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NoticeEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> null
            LoadType.APPEND -> getAppendKey()
        }

        if (page == null) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            // TODO: 나중에 중요 공지 보여줄 때 주석 해제하기
//            if (page == 0) {
//                loadAndSaveImportantNotices()
//            }
            val noticeResponse = noticeClient.fetchDepartmentNoticeList(
                shortName = shortName,
                page = page,
                size = ITEM_SIZE
            )

            val startDate = getAppStartedDate()
            val entities = noticeResponse.data.toEntityList(shortName, startDate)
            insertNotices(entities, page)

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
            size = ITEM_SIZE,
            important = true
        )

        val startDate = getAppStartedDate()
        val entities = importNoticesResponse.data.toEntityList(shortName, startDate)
        insertNotices(entities, 0)
    }

    private suspend fun insertNotices(noticeEntities: List<NoticeEntity>, page: Int) {
        noticeDao.insertAndUpdateDepartmentNotices(noticeEntities)
    }

    private fun getAppStartedDate(): String {
        // TODO: NoticeRepositoryImpl.transformRemoteData()에도 있는 이 로직을 PrefUtil로 옮기기
        return preferences.startDate?.takeIf { it.isNotEmpty() } ?: DateUtil.getToday().apply {
            preferences.startDate = this
        }
    }

    private suspend fun getAppendKey(): Int? {
        val noticeCount = noticeDao.getCountOfDepartmentNotice(shortName)
        // DB에 저장된 공지 개수로 지금까지 불러온 페이지 개수를 파악해 다음 page key를 계산한다
        return noticeCount / ITEM_SIZE
    }

    companion object {
        const val ITEM_SIZE = 20
    }
}
