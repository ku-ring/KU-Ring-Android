package com.ku_stacks.ku_ring.remote.notice

import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.remote.notice.response.DepartmentNoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.NoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.remote.notice.response.SubscribeListResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NoticeClient @Inject constructor(
    private val noticeService: NoticeService
) {
    suspend fun fetchNoticeList(
        type: String,
        offset: Int,
        max: Int
    ): NoticeListResponse = noticeService.fetchNoticeList(
        type,
        offset,
        max
    )

    suspend fun fetchSubscribe(token: String): SubscribeListResponse = noticeService.fetchSubscribeList(token)

    suspend fun saveSubscribe(
        token: String,
        subscribeRequest: SubscribeRequest
    ): DefaultResponse = noticeService.saveSubscribeList(
        token,
        subscribeRequest
    )

    suspend fun fetchDepartmentNoticeList(
        type: String = "dep",
        shortName: String,
        page: Int,
        size: Int,
        important: Boolean = false,
    ): DepartmentNoticeListResponse = noticeService.fetchDepartmentNoticeList(
        type,
        shortName,
        page,
        size,
        important
    )

    suspend fun fetchNoticeList(
        content: String
    ): SearchNoticeListResponse = noticeService.fetchNotices(content)
}
