package com.ku_stacks.ku_ring.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.source.NoticePagingSource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NoticeRepository @Inject constructor(
    private val noticeClient: NoticeClient
) {
    fun fetchNoticeList(type: String, offset: Int, max: Int): Single<NoticeListResponse> {
        return noticeClient.fetchNotice(type, offset, max)
    }

    fun getNotices(type: String): Flowable<PagingData<Notice>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,  //이것보다 PagingSource 에서 ItemCount 가 중요함
                enablePlaceholders = true
            ),
            pagingSourceFactory = { NoticePagingSource(type, noticeClient.noticeService) }
        ).flowable
    }
}