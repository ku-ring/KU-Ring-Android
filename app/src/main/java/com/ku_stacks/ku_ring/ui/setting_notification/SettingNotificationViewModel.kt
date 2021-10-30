package com.ku_stacks.ku_ring.ui.setting_notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.util.*
import kotlin.Comparator

@HiltViewModel
class SettingNotificationViewModel @Inject constructor(

) : ViewModel(){

    private val _subscriptionList = ArrayList<String>()
    val subscriptionList = MutableLiveData<ArrayList<String>>()

    private val _unSubscriptionList = ArrayList<String>()
    val unSubscriptionList = MutableLiveData<ArrayList<String>>()

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

    init {
        _subscriptionList.add("학사")
        _subscriptionList.add("장학")
        _subscriptionList.add("취창업")
        _subscriptionList.add("학생")
        subscriptionList.postValue(_subscriptionList)
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