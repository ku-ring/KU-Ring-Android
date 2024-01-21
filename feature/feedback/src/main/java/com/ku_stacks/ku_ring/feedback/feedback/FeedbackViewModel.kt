package com.ku_stacks.ku_ring.feedback.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.feedback.R
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui_util.SingleLiveEvent
import com.ku_stacks.ku_ring.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val analytics: EventAnalytics,
    private val firebaseMessaging: FirebaseMessaging
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _feedbackContent = MutableStateFlow("")
    val feedbackContent = _feedbackContent.asStateFlow()

    val textStatus = feedbackContent.map {
        when (it.length) {
            in 0..MIN_FEEDBACK_CONTENT_LENGTH -> FeedbackTextStatus.TOO_SHORT
            in MIN_FEEDBACK_CONTENT_LENGTH + 1..MAX_FEEDBACK_CONTENT_LENGTH -> FeedbackTextStatus.NORMAL
            else -> FeedbackTextStatus.TOO_LONG
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = FeedbackTextStatus.TOO_SHORT
    )

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

            if (textStatus.value == FeedbackTextStatus.TOO_SHORT) {
                _toastByResource.value = R.string.feedback_too_short
                return@addOnCompleteListener
            } else if (textStatus.value == FeedbackTextStatus.TOO_LONG) {
                _toastByResource.value = R.string.feedback_too_long
                return@addOnCompleteListener
            }

            val content = feedbackContent.value
            userRepository.sendFeedback(content)
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

    fun updateFeedbackContent(text: String) {
        _feedbackContent.value = text
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

        const val MIN_FEEDBACK_CONTENT_LENGTH = 4
        const val MAX_FEEDBACK_CONTENT_LENGTH = 256
    }
}
