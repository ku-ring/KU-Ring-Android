package com.ku_stacks.ku_ring.remote.notice

import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.remote.notice.response.CategoryResponse
import com.ku_stacks.ku_ring.remote.notice.response.DepartmentNoticeResponse
import com.ku_stacks.ku_ring.remote.notice.response.NoticeResponse
import com.ku_stacks.ku_ring.remote.notice.response.SearchNoticeDataResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class NoticeClient @Inject constructor(
    private val noticeService: NoticeService
) {
    suspend fun fetchNoticeList(
        type: String,
        page: Int,
        size: Int
    ): DefaultResponse<List<NoticeResponse>> = noticeService.fetchNoticeList(type, page, size)

    suspend fun fetchSubscribe(token: String): DefaultResponse<List<CategoryResponse>> =
        noticeService.fetchSubscribeList(token)

    suspend fun saveSubscribe(
        token: String,
        subscribeRequest: SubscribeRequest
    ): DefaultResponse<Unit> = noticeService.saveSubscribeList(
        token,
        subscribeRequest
    )

    suspend fun fetchDepartmentNoticeList(
        type: String = "dep",
        shortName: String,
        page: Int,
        size: Int,
        important: Boolean = false,
    ): DefaultResponse<List<DepartmentNoticeResponse>> = noticeService.fetchDepartmentNoticeList(
        type,
        shortName,
        page,
        size,
        important
    )

    suspend fun fetchNoticeList(
        content: String
    ): DefaultResponse<SearchNoticeDataResponse> = noticeService.fetchNotices(content)
}
