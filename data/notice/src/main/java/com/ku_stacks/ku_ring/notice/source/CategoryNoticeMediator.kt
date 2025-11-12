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
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.util.suspendRunCatching

@OptIn(ExperimentalPagingApi::class)
class CategoryNoticeMediator(
    private val categoryShortName: String,
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

        return suspendRunCatching {
            val noticeResponse = noticeClient.fetchNoticeList(categoryShortName, page, ITEM_SIZE)
            insertNotices(noticeResponse.noticeResponse)

            MediatorResult.Success(endOfPaginationReached = noticeResponse.noticeResponse.isEmpty())
        }.getOrElse {
            MediatorResult.Error(it)
        }
    }

    private suspend fun insertNotices(noticeResponses: List<NoticeResponse>) {
        val entities = noticeResponses.toEntities(getAppStartedDate())
        noticeDao.insertAndUpdateNotices(entities)
    }

    private fun getAppStartedDate(): String {
        return preferences.startDate?.takeIf { it.isNotEmpty() } ?: DateUtil.getToday().apply {
            preferences.startDate = this
        }
    }

    private suspend fun getAppendKey(): Int? {
        val category = WordConverter.convertShortNameToFullName(categoryShortName)
        val noticeCount = noticeDao.getCountOfNotice(category)
        // DB에 저장된 공지 개수로 지금까지 불러온 페이지 개수를 파악해 다음 page key를 계산한다
        return noticeCount / ITEM_SIZE
    }

    companion object {
        private const val ITEM_SIZE = 20
    }
}
