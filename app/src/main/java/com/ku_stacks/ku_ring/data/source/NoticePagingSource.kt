package com.ku_stacks.ku_ring.data.source

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.ku_stacks.ku_ring.data.api.NoticeService
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.mapper.transformNotice
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class NoticePagingSource constructor(
    private val type: String,
    private val service: NoticeService
) : RxPagingSource<Int, Notice>(){

    private val itemSize = 10

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Notice>> {
        val position = params.key ?: 0
        return service.fetchNoticeList(type, position, itemSize)
            .subscribeOn(Schedulers.io())
            .doOnError{
                Timber.e("network error of '$type' in NoticePagingSource : ${it.message}")
            }
            .retryWhen { flowable ->
                flowable.take(3).delay(5000, TimeUnit.MILLISECONDS)
            }
            .map { transformNotice(it) }
            .map { toLoadResult(it, position) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(data: List<Notice>, position: Int): LoadResult<Int, Notice> {
        return LoadResult.Page(
            data = data,
            prevKey = if (position == 0) null else position - itemSize, //TODO 다시생각
            nextKey = if (data.isEmpty()) null else position + itemSize
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Notice>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(itemSize) ?: anchorPage?.nextKey?.minus(itemSize)
        }
    }
}