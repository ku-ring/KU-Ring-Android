package com.ku_stacks.ku_ring.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoticeRepository
): ViewModel(){

    private val disposable = CompositeDisposable()

    private val _homeTabState = MutableLiveData<HomeTabState>()
    val homeTabState: LiveData<HomeTabState>
        get() = _homeTabState

    init {
        Timber.e("HomeViewModel injected")
    }

    fun updateNoticeTobeRead(notice: Notice) {
        disposable.add(
            repository.updateNoticeToBeRead(notice.articleId, notice.category)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //Timber.e("noticeRecord update true : $articleId")
                }, { Timber.e("noticeRecord update fail") })
        )

    }

    fun insertNotice(articleId: String, category: String) {
        disposable.add(
            repository.insertNotice(articleId = articleId, category = category)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //Timber.e("noticeRecord Insert true : $articleId")
                }, { Timber.e("noticeRecord Insert fail") })
        )
    }

    fun deleteDB() {
        repository.deleteDB()
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