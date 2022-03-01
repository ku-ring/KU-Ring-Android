package com.ku_stacks.ku_ring.ui.my_notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.data.mapper.toPushUiModelList
import com.ku_stacks.ku_ring.data.model.Push
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.PushRepository
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushDataUiModel
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushDateHeaderUiModel
import com.ku_stacks.ku_ring.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val pushRepository: PushRepository,
    private val noticeRepository: NoticeRepository,
    private val pref: PreferenceUtil
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _pushUiModelList = MutableLiveData<List<PushDataUiModel>>()
    val pushUiModelList: LiveData<List<PushDataUiModel>>
        get() = _pushUiModelList

    fun getMyNotification() {
        disposable.add(
            pushRepository.getMyNotification()
                .subscribeOn(Schedulers.io())
                .map { pushList -> pushList.toPushUiModelList() }
                .subscribe({
                    _pushUiModelList.postValue(it)
                }, {
                    Timber.e("getMyNotification failed : $it")
                })
        )
    }

    fun updateNotification(articleId: String) {
        disposable.add(
            pushRepository.updateNotification(articleId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    //Timber.e("update noti success with adapter")
                }, {
                    Timber.e("update noti failed with adapter")
                })
        )
    }

    fun hasSubscribingNotification(): Boolean {
        Timber.e("get subscription")
        val numberOfSubscribingList = pref.subscription?.size
        return numberOfSubscribingList != 0
    }

    fun deletePushDB(articleId: String) {
        pushRepository.deleteNotification(articleId)
    }

    fun deleteAllPushDB() {
        pushRepository.deleteAllNotification()
    }

    fun updateNoticeTobeRead(articleId: String, category: String) {
        disposable.add(
            noticeRepository.updateNoticeToBeRead(articleId, category)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.e("noticeRecord update true : $category")
                }, { Timber.e("noticeRecord update fail") })
        )
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}