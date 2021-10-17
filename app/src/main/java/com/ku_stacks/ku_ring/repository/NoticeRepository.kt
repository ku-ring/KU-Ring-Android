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
                pageSize = 10,
                enablePlaceholders = true
            //TODO 추가 configuration 고민
            ),
            pagingSourceFactory = { NoticePagingSource(type, noticeClient.noticeService) }
        ).flowable
    }
}