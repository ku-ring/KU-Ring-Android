package com.ku_stacks.ku_ring.ui.main.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.PushRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val pushRepository: PushRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _pushCount = MutableLiveData<Int>()
    val pushCount: LiveData<Int>
        get() = _pushCount

    init {
        Timber.e("NoticeViewModel injected")
        getNotificationCount()
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

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
            Timber.e("compositeDisposable disposed")
        }
    }
}