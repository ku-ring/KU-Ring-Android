package com.ku_stacks.ku_ring.feedback

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackViewModel
import com.ku_stacks.ku_ring.feedback.util.MainDispatcherRule
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.testutil.MockUtil
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.concurrent.Executor

class FeedbackViewModelTest {

    private lateinit var viewModel: FeedbackViewModel
    private val userRepository: UserRepository = MockUtil.mock()
    private val analytics: EventAnalytics = MockUtil.mock()
    private val preferenceUtil: PreferenceUtil = MockUtil.mock()

    private lateinit var successTask: Task<String>
    private val mockToken = "mockToken"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = FeedbackViewModel(userRepository, analytics, preferenceUtil)

        /** mocking for fcm dependency */
        successTask = object : Task<String>() {
            override fun isComplete(): Boolean {
                return true
            }

            override fun isSuccessful(): Boolean {
                return true
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun getResult(): String {
                return mockToken
            }

            override fun addOnCompleteListener(onCompleteListener: OnCompleteListener<String>): Task<String> {
                onCompleteListener.onComplete(successTask)
                return successTask
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): String {
                return mockToken
            }

            override fun getException(): java.lang.Exception {
                return Exception()
            }

            override fun addOnSuccessListener(onSucessListener: OnSuccessListener<in String>): Task<String> {
                return successTask
            }

            override fun addOnSuccessListener(
                executor: Executor,
                onSuccessListener: OnSuccessListener<in String>
            ): Task<String> {
                return successTask
            }

            override fun addOnSuccessListener(
                activity: Activity,
                onSuccessListener: OnSuccessListener<in String>
            ): Task<String> {
                return successTask
            }

            override fun addOnFailureListener(onFailureListener: OnFailureListener): Task<String> {
                return successTask
            }

            override fun addOnFailureListener(
                executor: Executor,
                onFailureListener: OnFailureListener
            ): Task<String> {
                return successTask
            }

            override fun addOnFailureListener(
                activity: Activity,
                onFailureListener: OnFailureListener
            ): Task<String> {
                return successTask
            }
        }
    }

    @Test
    fun `send Feedback Success Test`() = runTest {
        // given
        val mockFeedbackContent = "쿠링은 좋은 어플리케이션입니다."
        viewModel.updateFeedbackContent(mockFeedbackContent)
        viewModel.textStatus.first()

        Mockito.`when`(preferenceUtil.fcmToken).thenReturn(mockToken)

        val mockResponse = true to "성공"
        Mockito.`when`(userRepository.sendFeedback(mockFeedbackContent).getOrNull())
            .thenReturn(mockResponse)

        // when
        viewModel.sendFeedback()

        // then
        verify(userRepository, times(1)).sendFeedback(mockFeedbackContent)
        assertEquals(R.string.feedback_success, viewModel.toastByResource.value)
    }

    @Test
    fun `send Feedback Fail Test - too short`() = runTest {
        // given
        val mockFeedbackContent = "짧은"
        viewModel.updateFeedbackContent(mockFeedbackContent)
        viewModel.textStatus.first()

        Mockito.`when`(preferenceUtil.fcmToken).thenReturn(mockToken)

        // when
        viewModel.sendFeedback()

        // then
        verify(userRepository, times(0)).sendFeedback(any())
        assertEquals(R.string.feedback_too_short, viewModel.toastByResource.value)
    }

    @Test
    fun `send Feedback Fail Test - too long`() = runTest {
        // given
        var mockFeedbackContent = "1"
        repeat(256) {
            mockFeedbackContent += "a"
        }

        Mockito.`when`(preferenceUtil.fcmToken).thenReturn(mockToken)

        // when
        viewModel.updateFeedbackContent(mockFeedbackContent)
        viewModel.textStatus.first()

        viewModel.sendFeedback()

        // then
        assertEquals(257, mockFeedbackContent.length)
        verify(userRepository, times(0)).sendFeedback(any())
        assertEquals(R.string.feedback_too_long, viewModel.toastByResource.value)
    }

    @Test
    fun `send Feedback Fail Test - server response error`() = runTest {
        // given
        val mockFeedbackContent = "쿠링은 좋은 어플리케이션입니다."
        viewModel.updateFeedbackContent(mockFeedbackContent)
        viewModel.textStatus.first()

        Mockito.`when`(preferenceUtil.fcmToken).thenReturn(mockToken)

        val expectedResponseMsg = "알 수 없는 서버 오류"
        val mockResponse = false to expectedResponseMsg
        Mockito.`when`(userRepository.sendFeedback(mockFeedbackContent).getOrNull())
            .thenReturn(mockResponse)

        // when
        viewModel.sendFeedback()

        // then
        verify(userRepository, times(1)).sendFeedback(mockFeedbackContent)
        assertEquals(expectedResponseMsg, viewModel.toast.value)
    }
}
