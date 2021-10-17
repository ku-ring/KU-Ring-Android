package com.ku_stacks.ku_ring.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.repository.ITunesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: ITunesRepository
): ViewModel(){

    private val disposable = CompositeDisposable()

    private val _homeTabState = MutableLiveData<HomeTabState>()
    val homeTabState: LiveData<HomeTabState>
        get() = _homeTabState

    init {
        Timber.e("HomeViewModel injected")

        //TODO 로직은 init 외에서 호출도록 수정
//        repository.fetchTrackList(
//            term = "greenday",
//            entity = "song",
//            limit = 20,
//            offset = 0
//        )
//            .map { result -> result.results } //데이터 변환
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ result ->
//                Timber.e("fetchTrackList success ${result.size}")
//            }, { error ->
//                Timber.e("fetchTrackList fail : $error")
//            })
//            .apply { disposable.add(this) }

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