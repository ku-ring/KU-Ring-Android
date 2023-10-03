package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.api.DepartmentClient
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.request.SubscribeRequest
import com.ku_stacks.ku_ring.data.mapper.toDepartment
import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.ku_stacks.ku_ring.util.WordConverter
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SubscribeRepositoryImpl @Inject constructor(
    private val noticeClient: NoticeClient,
    private val departmentClient: DepartmentClient,
    private val pref: PreferenceUtil,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SubscribeRepository {
    override fun fetchSubscriptionFromRemote(token: String): Single<List<String>> {
        return noticeClient.fetchSubscribe(token)
            .map { response ->
                response.categoryList.map { it.koreanName }
            }
    }

    override fun saveSubscriptionToRemote(token: String, subscribeRequest: SubscribeRequest) {
        noticeClient.saveSubscribe(token, subscribeRequest)
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

    override suspend fun fetchSubscribedDepartments(): List<Department> {
        return try {
            pref.fcmToken?.let { fcmToken ->
                withContext(ioDispatcher) {
                    departmentClient.getSubscribedDepartments(fcmToken).data?.map {
                        it.toDepartment().copy(isSubscribed = true)
                    }
                }
            } ?: emptyList()
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    override suspend fun saveSubscribedDepartmentsToRemote(departments: List<Department>) {
        withContext(ioDispatcher) {
            pref.fcmToken?.let { fcmToken ->
                departmentClient.subscribeDepartments(fcmToken, departments.map { it.shortName })
            }
            Timber.d("Subscribed departments: ${departments.map { it.shortName }}")
        }
    }
}