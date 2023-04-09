package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import com.ku_stacks.ku_ring.data.api.response.DepartmentNoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.api.response.SubscribeListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NoticeClient @Inject constructor(
    val noticeService: NoticeService
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
        subscribeRequest: SubscribeRequest
    ): Single<DefaultResponse> = noticeService.saveSubscribeList(subscribeRequest)

    suspend fun fetchDepartmentNoticeList(
        type: String,
        shortName: String,
        offset: Int,
        max: Int,
    ): DepartmentNoticeListResponse =
        noticeService.fetchDepartmentNoticeList(type, shortName, offset, max)
}