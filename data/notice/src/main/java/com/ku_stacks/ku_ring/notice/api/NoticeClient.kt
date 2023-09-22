package com.ku_stacks.ku_ring.notice.api

import com.ku_stacks.ku_ring.notice.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.notice.api.response.DepartmentNoticeListResponse
import com.ku_stacks.ku_ring.notice.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.notice.api.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.notice.api.response.SubscribeListResponse
import com.ku_stacks.ku_ring.util.network.DefaultResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NoticeClient @Inject constructor(
    private val noticeService: NoticeService
) {
    fun fetchNoticeList(
        type: String,
        offset: Int,
        max: Int
    ): Single<NoticeListResponse> = noticeService.fetchNoticeList(type, offset, max)

    fun fetchSubscribe(
        token: String
    ): Single<SubscribeListResponse> = noticeService.fetchSubscribeList(token)

    fun saveSubscribe(
        token: String,
        subscribeRequest: SubscribeRequest
    ): Single<DefaultResponse> = noticeService.saveSubscribeList(token, subscribeRequest)

    suspend fun fetchDepartmentNoticeList(
        type: String = "dep",
        shortName: String,
        page: Int,
        size: Int,
        important: Boolean = false,
    ): DepartmentNoticeListResponse =
        noticeService.fetchDepartmentNoticeList(type, shortName, page, size, important)

    fun fetchNoticeList(
        content: String
    ): Single<SearchNoticeListResponse> = noticeService.fetchNotices(content)
}