package com.ku_stacks.ku_ring.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.PushRepository
import com.ku_stacks.ku_ring.ui.home.nav.HomeTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val pushRepository: PushRepository
): ViewModel(){

    private val disposable = CompositeDisposable()

    private val _homeTabState = MutableLiveData<HomeTabState>()
    val homeTabState: LiveData<HomeTabState>
        get() = _homeTabState

    private val _pushCount = MutableLiveData<Int>()
    val pushCount : LiveData<Int>
        get() = _pushCount

    init {
        Timber.e("HomeViewModel injected")
        getNotificationCount()
    }

    fun insertNotice(articleId: String, category: String) {
        disposable.add(
            noticeRepository.insertNotice(articleId = articleId, category = category)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //Timber.e("noticeRecord Insert true : $articleId")
                }, { Timber.e("noticeRecord Insert fail $it") })
        )
    }

    private fun getNotificationCount() {
        disposable.add(
            pushRepository.getNotificationCount()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _pushCount.postValue(it)
                }, {
                    Timber.e("getNotificationCount error $it")
                })
        )
    }

    fun deleteDB() {
        noticeRepository.deleteSharedPreference()
        noticeRepository.deleteAllNoticeRecord()
        pushRepository.deleteAllNotification()
    }

    fun onBchTabClick() {
        _homeTabState.postValue(HomeTabState.Bch)
    }
    fun onSchTabClick() {
        _homeTabState.postValue(HomeTabState.Sch)
    }
    fun onEmpTabClick() {
        _homeTabState.postValue(HomeTabState.Emp)
    }
    fun onNatTabClick() {
        _homeTabState.postValue(HomeTabState.Nat)
    }
    fun onStuTabClick() {
        _homeTabState.postValue(HomeTabState.Stu)
    }
    fun onIndTabClick() {
        _homeTabState.postValue(HomeTabState.Ind)
    }
    fun onNorTabClick() {
        _homeTabState.postValue(HomeTabState.Nor)
    }
    fun onLibTabClick() {
        _homeTabState.postValue(HomeTabState.Lib)
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
            Timber.e("compositeDisposable disposed")
        }
    }
}