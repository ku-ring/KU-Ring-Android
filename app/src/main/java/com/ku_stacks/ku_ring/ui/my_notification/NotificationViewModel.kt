package com.ku_stacks.ku_ring.ui.my_notification

import androidx.lifecycle.ViewModel
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

    init {
        Timber.e("NotificationViewModel injected")
    }

    fun getMyNotification() {
        repository.getMyNotification()
            .subscribeOn(Schedulers.io())
            .subscribe({

            }, {
                Timber.e("getMyNotification failed : $it")
            })
    }

    //testing
    fun showPushDB() {
        repository.getMyNotification()
            .subscribeOn(Schedulers.io())
            .subscribe({
                for(push in it){
                    Timber.e("pushEntity : ${push.articleId}, ${push.isNew}")
                }
            }, {
                Timber.e("get pushEntity fail")
            })
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}