package com.ku_stacks.ku_ring.ui.home.category._0_Bachelor

import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.repository.NoticeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BachelorViewModel @Inject constructor(
    private val repository: NoticeRepository
): ViewModel() {

    private val disposable = CompositeDisposable()

    init {
        Timber.e("BachelorViewModel injected")
    }

    fun loadNotice() {
        repository.fetchNoticeList(type = "bch",
            offset = 0,
            max = 10
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                Timber.e("fetchNoticeList success : ${result}")
            }, { error ->
                Timber.e("fetchNoticeList fail : $error")
            })
            .apply { disposable.add(this) }
    }


}