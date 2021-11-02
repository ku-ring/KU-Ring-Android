package com.ku_stacks.ku_ring.ui.feedback

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
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

    private val _quit = SingleLiveEvent<Unit>()
    val quit: SingleLiveEvent<Unit>
        get() = _quit

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

            feedbackClient.sendFeedback(
                Feedback(
                    token = fcmToken,
                    content = content
                )
            )
                .filter { it.isSuccess == true }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.e("feedback success content : $content")
                    _quit.call()
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