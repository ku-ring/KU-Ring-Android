package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.util.WordConverter
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SubscribeRepository @Inject constructor(
    private val noticeClient: NoticeClient,
) {
    fun getSubscribeList(token: String): Single<List<String>> {
        return noticeClient.fetchSubscribe(token)
            .map { response ->
                response.categoryList.map { category ->
                    WordConverter.convertEnglishToKorean(category)
                }
            }
    }
}