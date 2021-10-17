package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NoticeClient @Inject constructor(
    val noticeService: NoticeService
) {
    fun fetchNotice(
        type: String,
        offset: Int,
        max: Int
    ): Single<NoticeListResponse> = noticeService.fetchNoticeList(type, offset, max)
}