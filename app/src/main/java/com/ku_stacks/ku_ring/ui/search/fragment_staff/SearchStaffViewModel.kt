package com.ku_stacks.ku_ring.ui.search.fragment_staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.data.websocket.SearchClient
import com.ku_stacks.ku_ring.data.websocket.response.StaffResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchStaffViewModel @Inject constructor(

) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val searchStaffClient = SearchClient()

    private val _staffList = MutableLiveData<List<StaffResponse>>()
    val staffList: LiveData<List<StaffResponse>>
        get() = _staffList

    init {
        startWebSocket()
        subscribeStaff()
    }

    fun searchStaff(keyword: String) {
        Timber.e("isOpen : ${searchStaffClient.isOpen()}, isPreparing: ${searchStaffClient.isPreparing()}")
        if(!searchStaffClient.isOpen()){
            if(!searchStaffClient.isPreparing()) {
                startWebSocket()
            }
            searchStaffClient.setLastKeyword(keyword)
            return
        }
        searchStaffClient.searchStaff(keyword)
    }

    private fun subscribeStaff() {
        disposable.add(
            searchStaffClient.subscribeStaff().subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.isSuccess) {
                        _staffList.postValue(it.staffList)
                    }
                }, {
                    Timber.e("subscribe error : $it")
                })
        )
    }

    private fun startWebSocket() {
        searchStaffClient.initWebSocket()
        Timber.e("startWebSocket")
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}