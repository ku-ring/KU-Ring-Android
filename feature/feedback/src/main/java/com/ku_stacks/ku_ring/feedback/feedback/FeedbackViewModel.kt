package com.ku_stacks.ku_ring.feedback.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import com.ku_stacks.ku_ring.feedback.R
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
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

    private val _quit = MutableSharedFlow<Unit>()
    val quit = _quit.asSharedFlow()

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    private val _toastByResource = MutableSharedFlow<Int>()
    val toastByResource = _toastByResource.asSharedFlow()

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
                _toastByResource.emit(R.string.feedback_cannot_send)
                return@launch
            }

            if (textStatus.value == FeedbackTextStatus.TOO_SHORT) {
                _toastByResource.emit(R.string.feedback_too_short)
                return@launch
            } else if (textStatus.value == FeedbackTextStatus.TOO_LONG) {
                _toastByResource.emit(R.string.feedback_too_long)
                return@launch
            }

            val content = feedbackContent.value

            userRepository.sendFeedback(content).onSuccess {
                val (isSuccess, resultMessage) = it
                if (isSuccess) {
                    _toastByResource.emit(R.string.feedback_success)
                    _quit.emit(Unit)
                } else {
                    _toast.emit(resultMessage)
                }
            }.onFailure {
                _toastByResource.emit(R.string.feedback_cannot_send)
            }
        }
    }

    fun updateFeedbackContent(text: String) {
        _feedbackContent.value = text
    }

    fun closeFeedback() {
        viewModelScope.launch {
            analytics.click(
                "close feedback button",
                "FeedbackActivity"
            )
            _quit.emit(Unit)
        }
    }

    companion object {
        private val className: String = FeedbackViewModel::class.java.simpleName

        const val MIN_FEEDBACK_CONTENT_LENGTH = 4
        const val MAX_FEEDBACK_CONTENT_LENGTH = 256
    }
}
