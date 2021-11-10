package com.ku_stacks.ku_ring.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.data.websocket.SearchClient
import com.ku_stacks.ku_ring.data.websocket.response.SearchNoticeResponse
import com.ku_stacks.ku_ring.data.websocket.response.StaffResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val searchClient = SearchClient()

    private val _staffList = MutableLiveData<List<StaffResponse>>()
    val staffList: LiveData<List<StaffResponse>>
        get() = _staffList

    private val _noticeList = MutableLiveData<List<SearchNoticeResponse>>()
    val noticeList: LiveData<List<SearchNoticeResponse>>
        get() = _noticeList

    init {
        connectWebSocket()
        subscribeStaff()
        subscribeNotice()
        makeHeartBeat()
    }

    private fun connectWebSocket() {
        searchClient.initWebSocket()
        Timber.e("connectWebSocket")
    }

    fun searchStaff(keyword: String) {
        Timber.e("isOpen : ${searchClient.isOpen()}, isPreparing: ${searchClient.isPreparing()}")
        if(!searchClient.isOpen()){
            if(!searchClient.isPreparing()) {
                connectWebSocket()
            }
            searchClient.setLastKeyword(keyword)
            return
        }
        searchClient.searchStaff(keyword)
    }

    fun searchNotice(keyword: String) {
        Timber.e("isOpen : ${searchClient.isOpen()}, isPreparing: ${searchClient.isPreparing()}")
        if(!searchClient.isOpen()){
            if(!searchClient.isPreparing()) {
                connectWebSocket()
            }
            searchClient.setLastKeyword(keyword)
            return
        }
        searchClient.searchNotice(keyword)
    }

    private fun subscribeStaff() {
        disposable.add(
            searchClient.subscribeStaff().subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.e("subscribeStaff result : ${it.staffList.size}")
                    if (it.isSuccess) {
                        _staffList.postValue(it.staffList)
                    }
                }, {
                    Timber.e("subscribe staff error : $it")
                })
        )
    }

    private fun subscribeNotice() {
        disposable.add(
            searchClient.subscribeNotice().subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.e("subscribeNotice result : ${it.noticeList.size}")
                    if(it.isSuccess) {
                        _noticeList.postValue(it.noticeList)
                    }
                }, {
                    Timber.e("subscribe notice error : $it")
                })
        )
    }

    fun disconnectWebSocket() {
        searchClient.disconnectWebSocket()
        Timber.e("disconnectWebSocket")
    }

    fun connectWebSocketIfDisconnected() {
        if(!searchClient.isOpen()){
            if(!searchClient.isPreparing()) {
                connectWebSocket()
            }
        }
    }

    private fun makeHeartBeat() {
        disposable.add(
            Observable.timer(30, TimeUnit.SECONDS)
                .filter { searchClient.isOpen() } //서버가 열려있는 경우에만 보내야함
                .map { searchClient.makeHeartBeat() }
                .repeat()
                .subscribeOn(Schedulers.io())
                .doOnError{Timber.e("make heartbeat failed")}
                .subscribe ({
                    Timber.e("make heartbeat success")
                }, {
                    Timber.e("make heartbeat failed : $it")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

}