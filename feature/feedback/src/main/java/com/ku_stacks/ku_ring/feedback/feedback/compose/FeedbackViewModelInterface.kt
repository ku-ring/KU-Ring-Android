package com.ku_stacks.ku_ring.feedback.feedback.compose

import com.ku_stacks.ku_ring.feedback.feedback.FeedbackTextStatus
import kotlinx.coroutines.flow.StateFlow

interface FeedbackViewModelInterface {
    val feedbackContent: StateFlow<String>
    val textStatus: StateFlow<FeedbackTextStatus>

    fun closeFeedback() = Unit
    fun updateFeedbackContent(text: String) = Unit
    fun sendFeedback() = Unit
}
