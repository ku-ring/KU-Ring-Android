package com.ku_stacks.ku_ring.ui.feedback

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor() : ViewModel() {

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
        Timber.e("send content : ${feedbackContent.value}")
        analytics.click("send feedback button", "FeedbackActivity")

        //api 성공시
        _quit.call()
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