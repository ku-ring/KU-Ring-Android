package com.ku_stacks.ku_ring.ui.setting_notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.data.entity.Subscribe
import com.ku_stacks.ku_ring.repository.SubscribeRepository
import com.ku_stacks.ku_ring.ui.SingleLiveEvent
import com.ku_stacks.ku_ring.util.WordConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import java.util.*
import kotlin.Comparator

@HiltViewModel
class SettingNotificationViewModel @Inject constructor(
    private val repository: SubscribeRepository
) : ViewModel(){
    private val disposable = CompositeDisposable()

    private val _subscriptionList = ArrayList<String>()
    val subscriptionList = MutableLiveData<ArrayList<String>>()

    private val _unSubscriptionList = ArrayList<String>()
    val unSubscriptionList = MutableLiveData<ArrayList<String>>()

    private val _quit = SingleLiveEvent<Unit>()
    val quit: SingleLiveEvent<Unit>
        get() = _quit

    private var fcmToken: String? = null

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(!task.isSuccessful){
                Timber.e("Firebase instanceId fail : ${task.exception}")
                throw RuntimeException("Failed to get Fcm Token error")
            }
            fcmToken = task.result
            if (fcmToken == null) {
                throw RuntimeException("Fcm Token is null!")
            }

            _unSubscriptionList.addAll(listOf(
                "학사", "장학", "취창업", "국제", "학생", "산학", "일반", "도서관"
            ))

            disposable.add(
                repository.getSubscribeList(fcmToken!!)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { initialSort(it) },
                        { Timber.e("getSubscribeList fail $it") })
            )
        }
    }

    fun saveSubscribe() {
        fcmToken?.let {
            disposable.add(
                repository.saveSubscribe(
                    Subscribe(it, _subscriptionList.toList().map { category ->
                        WordConverter.convertKoreanToEnglish(category)
                    })
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        if (response.isSuccess) {
                            Timber.e("saveSubscribe success")
                            _quit.call()
                        } else {
                            Timber.e("saveSubscribe failed ${response.resultCode}")
                        }
                    }, {
                        Timber.e("saveSubscribe failed $it")
                    })
            )
        }
    }

    fun removeSubscription(category: String) {
        _subscriptionList.remove(category)
        _subscriptionList.sortWith(CategoryComparator)
        subscriptionList.postValue(_subscriptionList)
    }

    fun removeUnSubscription(category: String) {
        _unSubscriptionList.remove(category)
        _unSubscriptionList.sortWith(CategoryComparator)
        unSubscriptionList.postValue(_unSubscriptionList)
    }

    fun addSubscription(category: String) {
        _subscriptionList.add(category)
        _subscriptionList.sortWith(CategoryComparator)
        subscriptionList.postValue(_subscriptionList)
    }

    fun addUnSubscription(category: String) {
        _unSubscriptionList.add(category)
        _unSubscriptionList.sortWith(CategoryComparator)
        unSubscriptionList.postValue(_unSubscriptionList)
    }

    private fun initialSort(subscribingList: List<String>) {
        for(str in subscribingList){
            _subscriptionList.add(str)
            _unSubscriptionList.remove(str)
        }
        subscriptionList.postValue(_subscriptionList)
        unSubscriptionList.postValue(_unSubscriptionList)
    }

    /*
    안드로이드에서 priority queue 는 api24부터 지원해서(현재 minSdk = 23)
    변경될때마다 정렬하는 방식 채택
    getPriority()가 낮을수록 앞쪽으로 정렬
     */
    object CategoryComparator : Comparator<String> {
        private fun getPriority(category: String): Int {
            return when (category) {
                "학사" -> 1
                "장학" -> 2
                "취창업" -> 3
                "국제" -> 4
                "학생" -> 5
                "산학" -> 6
                "일반" -> 7
                "도서관" -> 8
                else -> 100
            }
        }

        override fun compare(a: String, b: String): Int {
            return getPriority(a) - getPriority(b)
        }
    }
}