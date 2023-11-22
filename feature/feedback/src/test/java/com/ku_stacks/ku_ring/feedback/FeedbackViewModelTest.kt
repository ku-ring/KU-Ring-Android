package com.ku_stacks.ku_ring.feedback

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.ku_stacks.ku_ring.feedback.feedback.FeedbackViewModel
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import com.ku_stacks.ku_ring.thirdparty.firebase.analytics.EventAnalytics
import com.ku_stacks.ku_ring.user.repository.UserRepository
import com.ku_stacks.ku_ring.util.MockUtil
import com.ku_stacks.ku_ring.util.SchedulersTestRule
import io.reactivex.rxjava3.core.Single
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
    private val userRepository: UserRepository = MockUtil.mock(UserRepository::class.java)
    private val analytics: EventAnalytics = MockUtil.mock(EventAnalytics::class.java)
    private val firebaseMessaging: FirebaseMessaging = MockUtil.mock(FirebaseMessaging::class.java)

    private lateinit var successTask: Task<String>
    private val mockToken = "mockToken"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulersRule = SchedulersTestRule()

    @Before
    fun setup() {
        viewModel = FeedbackViewModel(userRepository, analytics, firebaseMessaging)

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
    fun `send Feedback Success Test`() {
        // given
        val mockFeedbackContent = "쿠링은 좋은 어플리케이션입니다."
        viewModel.feedbackContent.value = mockFeedbackContent

        Mockito.`when`(firebaseMessaging.token).thenReturn(successTask)

        val mockResponse = DefaultResponse(
            resultMsg = "성공",
            resultCode = 200,
            data = null,
        )
        Mockito.`when`(userRepository.sendFeedback(mockFeedbackContent))
            .thenReturn(Single.just(mockResponse))

        // when
        viewModel.sendFeedback()

        // then
        verify(userRepository, times(1)).sendFeedback(mockFeedbackContent)
        assertEquals(R.string.feedback_success, viewModel.toastByResource.value)
    }

    @Test
    fun `send Feedback Fail Test - too short`() {
        // given
        val mockFeedbackContent = "짧은"
        viewModel.feedbackContent.value = mockFeedbackContent

        Mockito.`when`(firebaseMessaging.token).thenReturn(successTask)

        // when
        viewModel.sendFeedback()

        // then
        verify(userRepository, times(0)).sendFeedback(any())
        assertEquals(R.string.feedback_too_short, viewModel.toastByResource.value)
    }

    @Test
    fun `send Feedback Fail Test - too long`() {
        // given
        var mockFeedbackContent = "1"
        repeat(256) {
            mockFeedbackContent += "a"
        }

        viewModel.feedbackContent.value = mockFeedbackContent

        Mockito.`when`(firebaseMessaging.token).thenReturn(successTask)

        // when
        viewModel.sendFeedback()

        // then
        assertEquals(257, mockFeedbackContent.length)
        verify(userRepository, times(0)).sendFeedback(any())
        assertEquals(R.string.feedback_too_long, viewModel.toastByResource.value)
    }

    @Test
    fun `send Feedback Fail Test - server response error`() {
        // given
        val mockFeedbackContent = "쿠링은 좋은 어플리케이션입니다."
        viewModel.feedbackContent.value = mockFeedbackContent

        Mockito.`when`(firebaseMessaging.token).thenReturn(successTask)

        val expectedResponseMsg = "알 수 없는 서버 오류"
        val mockResponse = DefaultResponse(
            resultMsg = expectedResponseMsg,
            resultCode = 500,
            data = null,
        )
        Mockito.`when`(userRepository.sendFeedback(mockFeedbackContent))
            .thenReturn(Single.just(mockResponse))

        // when
        viewModel.sendFeedback()

        // then
        verify(userRepository, times(1)).sendFeedback(mockFeedbackContent)
        assertEquals(expectedResponseMsg, viewModel.toast.value)
    }
}