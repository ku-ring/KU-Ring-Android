package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NoticeRepository @Inject constructor(
    private val noticeClient: NoticeClient
) {
    fun fetchNoticeList(type: String, offset: Int, max: Int): Single<NoticeListResponse> {
        return noticeClient.fetchNotice(type, offset, max)
    }
}