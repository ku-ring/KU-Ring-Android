package com.ku_stacks.ku_ring.remote.util

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<Value : Any> : PagingSource<Int, Value>() {
    /**
     * API를 조회해 [LoadResult.Page]로 래핑한 결과를 반환합니다.
     *
     * @param cursor 조회할 다음 페이지
     * @param size 조회할 페이지의 크기
     * @return [PagingRawData]로 래핑한 조회 결과
     * @throws IllegalStateException 조회에 실패하면 메시지를 포함한 예외를 던집니다.
     */
    abstract suspend fun provideData(cursor: Int, size: Int): PagingRawData<Value>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        return try {
            val cursor = params.key ?: 0
            val size = params.loadSize
            val (items, hasNext, nextCursor) = provideData(cursor, size)

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = if (hasNext) nextCursor else null
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

data class PagingRawData<T>(
    val items: List<T>,
    val hasNext: Boolean,
    val nextCursor: Int?,
)
