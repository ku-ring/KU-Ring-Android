package com.ku_stacks.ku_ring.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.data.api.FeedbackClient
import com.ku_stacks.ku_ring.data.api.request.FeedbackRequest
import com.ku_stacks.ku_ring.ui_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackClient: FeedbackClient,
    private val analytics: EventAnalytics,
    private val firebaseMessaging: FirebaseMessaging
) : ViewModel() {

    private val disposable = CompositeDisposable()

    val feedbackContent = MutableLiveData("")
    val canSendFeedback = MutableLiveData(false)

    private val _quit = SingleLiveEvent<Unit>()
    val quit: LiveData<Unit>
        get() = _quit

    private val _toast = SingleLiveEvent<String>()
    val toast: LiveData<String>
        get() = _toast

    private val _toastByResource = SingleLiveEvent<Int>()
    val toastByResource: LiveData<Int>
        get() = _toastByResource

    init {
        Timber.e("FeedbackViewModel injected")
    }

    fun sendFeedback() {
        analytics.click("send feedback button", "FeedbackActivity")

        firebaseMessaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("Firebase get Fcm Token error : ${task.exception}")
                analytics.errorEvent("Failed to get Fcm Token error : ${task.exception}", className)
                _toastByResource.postValue(R.string.feedback_cannot_send)
                return@addOnCompleteListener
            }
            val fcmToken = task.result
            if (fcmToken == null) {
                analytics.errorEvent("Fcm Token is null!", className)
                _toastByResource.postValue(R.string.feedback_cannot_send)
                return@addOnCompleteListener
            }

            val content = feedbackContent.value ?: ""
            if (content.length < 5) {
                _toastByResource.value = R.string.feedback_too_short
                return@addOnCompleteListener
            } else if (content.length > 256) {
                _toastByResource.value = R.string.feedback_too_long
                return@addOnCompleteListener
            }

            feedbackClient.sendFeedback(
                token = fcmToken,
                feedbackRequest = FeedbackRequest(content = content),
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d(it.resultCode.toString())
                    if (it.isSuccess) {
                        Timber.e("feedback success content : $content")
                        _toastByResource.value = R.string.feedback_success
                        _quit.call()
                    } else {
                        Timber.e("feedback failed : ${it.resultCode}, ${it.resultMsg}")
                        _toast.value = it.resultMsg
                    }
                }, {
                    Timber.e("feedback error : $it")
                    _toastByResource.value = R.string.network_error
                })
        }
    }

    fun closeFeedback() {
        analytics.click("close feedback button", "FeedbackActivity")
        _quit.call()
    }

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    companion object {
        private val className: String = FeedbackViewModel::class.java.simpleName
    }
}