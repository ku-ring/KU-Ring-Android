package com.ku_stacks.kustack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.kustack.data.api.response.TrackListResponse
import com.ku_stacks.kustack.repository.ITunesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
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

        repository.fetchTrackList(
            term = "greenday",
            entity = "song",
            limit = 20,
            offset = 0
        )
            .map { result -> result.results }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Timber.e("fetchTrackList success ${result.size}")
            }, { error ->
                Timber.e("fetchTrackList fail : $error")
            })
            .apply { disposable.add(this) }

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