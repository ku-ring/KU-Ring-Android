package com.ku_stacks.ku_ring.feedback.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.feedback.R
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.ui_util.SingleLiveEvent
import com.ku_stacks.ku_ring.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val analytics: EventAnalytics,
    private val preferenceUtil: PreferenceUtil,
) : ViewModel() {

    private val _feedbackContent = MutableStateFlow("")
    val feedbackContent = _feedbackContent.asStateFlow()

    val textStatus = feedbackContent.map {
        when (it.length) {
            0 -> FeedbackTextStatus.INITIAL
            in 1..MIN_FEEDBACK_CONTENT_LENGTH -> FeedbackTextStatus.TOO_SHORT
            in MIN_FEEDBACK_CONTENT_LENGTH + 1..MAX_FEEDBACK_CONTENT_LENGTH -> FeedbackTextStatus.NORMAL
            else -> FeedbackTextStatus.TOO_LONG
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = FeedbackTextStatus.INITIAL
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

    fun sendFeedback() {
        analytics.click(
            "send feedback button",
            "FeedbackActivity"
        )
        viewModelScope.launch {
            val fcmToken = preferenceUtil.fcmToken
            if (fcmToken.isEmpty()) {
                analytics.errorEvent(
                    "Fcm Token is null!",
                    className
                )
                _toastByResource.postValue(R.string.feedback_cannot_send)
                return@launch
            }

            if (textStatus.value == FeedbackTextStatus.TOO_SHORT) {
                _toastByResource.value = R.string.feedback_too_short
                return@launch
            } else if (textStatus.value == FeedbackTextStatus.TOO_LONG) {
                _toastByResource.value = R.string.feedback_too_long
                return@launch
            }

            val content = feedbackContent.value

            userRepository.sendFeedback(content).onSuccess {
                if (it.isSuccess) {
                    _toastByResource.value = R.string.feedback_success
                    _quit.call()
                } else {
                    _toast.value = it.resultMsg
                }
            }.onFailure {
                _toastByResource.postValue(R.string.feedback_cannot_send)
            }
        }
    }

    fun updateFeedbackContent(text: String) {
        _feedbackContent.value = text
    }

    fun closeFeedback() {
        analytics.click(
            "close feedback button",
            "FeedbackActivity"
        )
        _quit.call()
    }

    companion object {
        private val className: String = FeedbackViewModel::class.java.simpleName

        const val MIN_FEEDBACK_CONTENT_LENGTH = 4
        const val MAX_FEEDBACK_CONTENT_LENGTH = 256
    }
}
