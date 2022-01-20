package com.ku_stacks.ku_ring.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.data.mapper.toNoticeList
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.model.Staff
import com.ku_stacks.ku_ring.data.mapper.toStaffList
import com.ku_stacks.ku_ring.data.websocket.SearchClient
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val searchClient = SearchClient()

    private val _staffList = MutableLiveData<List<Staff>>()
    val staffList: LiveData<List<Staff>>
        get() = _staffList

    private val _noticeList = MutableLiveData<List<Notice>>()
    val noticeList: LiveData<List<Notice>>
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
        Timber.e("in searchStaff -> isOpen : ${searchClient.isOpen()}, isPreparing: ${searchClient.isPreparing()}")
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
        Timber.e("in searchNotice -> isOpen : ${searchClient.isOpen()}, isPreparing: ${searchClient.isPreparing()}")
        if(!searchClient.isOpen()){
            if(!searchClient.isPreparing()) {
                connectWebSocket()
            }
            searchClient.setLastKeyword(keyword)
            return
        }
        searchClient.searchNotice(keyword)
    }

    fun clearNoticeList() {
        _noticeList.postValue(emptyList())
    }

    fun clearStaffList() {
        _staffList.postValue(emptyList())
    }

    private fun subscribeStaff() {
        disposable.add(
            searchClient.publishStaff.subscribeOn(Schedulers.io())
                .filter { it.isSuccess }
                .map { staffListResponse -> staffListResponse.toStaffList() }
                .subscribe({
                    _staffList.postValue(it)
                }, {
                    Timber.e("subscribe staff error : $it")
                })
        )
    }

    private fun subscribeNotice() {
        disposable.add(
            searchClient.publishNotice.subscribeOn(Schedulers.io())
                .filter { it.isSuccess }
                .map { noticeListResponse -> noticeListResponse.toNoticeList() }
                .subscribe({
                    _noticeList.postValue(it)
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

    fun updateNoticeTobeRead(notice: Notice) {
        disposable.add(
            noticeRepository.updateNoticeToBeRead(notice.articleId, notice.category)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //Timber.e("noticeRecord update true : $category")
                }, { Timber.e("noticeRecord update fail") })
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