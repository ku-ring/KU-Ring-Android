package com.ku_stacks.ku_ring.my_notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.my_notification.mapper.toPushUiModelList
import com.ku_stacks.ku_ring.my_notification.ui_model.PushDataUiModel
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.push.repository.PushRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val pushRepository: PushRepository,
    private val pref: PreferenceUtil
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _pushUiModelList = MutableLiveData<List<PushDataUiModel>>()
    val pushUiModelList: LiveData<List<PushDataUiModel>>
        get() = _pushUiModelList

    fun getMyNotificationList() {
        disposable.add(
            pushRepository.getMyNotificationList()
                .subscribeOn(Schedulers.io())
                .map { pushList -> pushList.toPushUiModelList() }
                .subscribe({
                    _pushUiModelList.postValue(it)
                }, { })
        )
    }

    fun updateNotificationToBeOld(articleId: String) {
        disposable.add(
            pushRepository.updateNotificationAsOld(articleId)
                .subscribeOn(Schedulers.io())
                .subscribe({ }, { })
        )
    }

    fun hasSubscribingNotification(): Boolean {
        val numberOfSubscribingList = pref.subscription.size
        return numberOfSubscribingList != 0
    }

    fun deletePushDB(articleId: String) {
        pushRepository.deleteNotification(articleId)
    }

    fun deleteAllPushDB() {
        pushRepository.deleteAllNotification()
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}