package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.response.DefaultResponse
import com.ku_stacks.ku_ring.data.entity.Subscribe
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.ku_stacks.ku_ring.util.WordConverter
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SubscribeRepository @Inject constructor(
    private val noticeClient: NoticeClient,
    private val pref: PreferenceUtil
) {
    fun getSubscribeList(token: String): Single<List<String>> {
        return noticeClient.fetchSubscribe(token)
            .map { response ->
                response.categoryList.map { category ->
                    WordConverter.convertEnglishToKorean(category)
                }
            }
    }

    fun saveSubscribe(subscribe: Subscribe): Single<DefaultResponse> {
        return noticeClient.saveSubscribe(subscribe)
    }

    fun saveSubscriptionToLocal(stringArray: ArrayList<String>) {
        val stringSet = stringArray.map {
            WordConverter.convertKoreanToShortEnglish(it)
        }.toSet()

        pref.setSubscription(stringSet)
    }
}