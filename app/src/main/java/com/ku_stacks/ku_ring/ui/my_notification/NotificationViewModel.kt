package com.ku_stacks.ku_ring.ui.my_notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.repository.PushRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: PushRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _pushList = MutableLiveData<List<PushEntity>>()
    val pushList: LiveData<List<PushEntity>>
        get() = _pushList

    init {
        Timber.e("NotificationViewModel injected")
        //repository.deleteAllNotification()
    }

    fun getMyNotification() {
        disposable.add(
            repository.getMyNotification()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _pushList.postValue(it)
                }, {
                    Timber.e("getMyNotification failed : $it")
                })
        )
    }

    fun updateNotification(articleId: String) {
        disposable.add(
            repository.updateNotification(articleId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //Timber.e("update noti success with adapter")
                }, {
                    Timber.e("update noti failed with adapter")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}