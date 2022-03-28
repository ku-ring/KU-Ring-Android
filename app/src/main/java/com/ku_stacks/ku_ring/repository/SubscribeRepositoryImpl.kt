package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.ku_stacks.ku_ring.util.WordConverter
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SubscribeRepositoryImpl @Inject constructor(
    private val noticeClient: NoticeClient,
    private val pref: PreferenceUtil
) : SubscribeRepository {
    override fun fetchSubscriptionFromRemote(token: String): Single<List<String>> {
        return noticeClient.fetchSubscribe(token)
            .map { response ->
                response.categoryList.map { category ->
                    WordConverter.convertEnglishToKorean(category)
                }
            }
    }

    override fun saveSubscriptionToRemote(subscribeRequest: SubscribeRequest) {
        noticeClient.saveSubscribe(subscribeRequest)
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccess) {
                    Timber.e("saveSubscribe success")
                    pref.firstRunFlag = false
                } else {
                    Timber.e("saveSubscribe failed ${response.resultCode}")
                }
            }, {
                Timber.e("saveSubscribe failed $it")
            })
    }

    override fun getSubscriptionFromLocal(): Set<String> {
        return pref.subscription ?: emptySet()
    }

    override fun saveSubscriptionToLocal(stringArray: ArrayList<String>) {
        val stringSet = stringArray.map {
            WordConverter.convertKoreanToShortEnglish(it)
        }.toSet()

        pref.subscription = stringSet
    }
}