package com.ku_stacks.ku_ring.notice.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.notice.mapper.toNoticeList
import com.ku_stacks.ku_ring.remote.notice.NoticeClient

class NoticePagingSource(
    private val type: String,
    private val client: NoticeClient
) : PagingSource<Int, Notice>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notice> {
        val position = params.key ?: 0
        val pages = runCatching {
            client.fetchNoticeList(
                type,
                position,
                ITEM_SIZE
            )
        }.getOrElse {
            return LoadResult.Error(it)
        }.toNoticeList(type)

        return LoadResult.Page(
            data = pages,
            prevKey = if (position == 0) null else position - ITEM_SIZE,
            nextKey = if (pages.isEmpty()) null else position + ITEM_SIZE
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Notice>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(ITEM_SIZE) ?: anchorPage?.nextKey?.minus(ITEM_SIZE)
        }
    }

    companion object {
        const val ITEM_SIZE = 20
    }
}
