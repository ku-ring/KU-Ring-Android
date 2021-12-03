package com.ku_stacks.ku_ring.ui.feedback

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.data.api.FeedbackClient
import com.ku_stacks.ku_ring.data.entity.Feedback
import com.ku_stacks.ku_ring.ui.SingleLiveEvent
import com.ku_stacks.ku_ring.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackClient: FeedbackClient
) : ViewModel() {

    @Inject
    lateinit var analytics : EventAnalytics

    private val disposable = CompositeDisposable()

    val feedbackContent = MutableLiveData("")
    val canSendFeedback = MutableLiveData(false)

    private val _quit = SingleLiveEvent<Unit>()
    val quit: SingleLiveEvent<Unit>
        get() = _quit

    private val _toast = SingleLiveEvent<String>()
    val toast: SingleLiveEvent<String>
        get() = _toast

    private val _toastByResource = SingleLiveEvent<Int>()
    val toastByResource: SingleLiveEvent<Int>
        get() = _toastByResource

    init {
        Timber.e("FeedbackViewModel injected")
    }

    fun sendFeedback() {
        analytics.click("send feedback button", "FeedbackActivity")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("Firebase instanceId fail : ${task.exception}")
                throw RuntimeException("Failed to get Fcm Token error")
            }

            val fcmToken = task.result ?: throw RuntimeException("Fcm Token is null!")
            val content = feedbackContent.value ?: return@addOnCompleteListener

            if (content.length < 5) {
                _toastByResource.value = R.string.feedback_too_short
                return@addOnCompleteListener
            } else if (content.length > 256) {
                _toastByResource.value = R.string.feedback_too_long
                return@addOnCompleteListener
            }

            feedbackClient.sendFeedback(
                Feedback(
                    token = fcmToken,
                    content = content
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccess) {
                        _toastByResource.value = R.string.feedback_success
                        Timber.e("feedback success content : $content")
                        _quit.call()
                    } else {
                        Timber.e("feedback failed : ${it.resultCode}, ${it.resultMsg}")
                        _toast.value = it.resultMsg
                    }
                }, {
                    Timber.e("feedback fail")
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
}