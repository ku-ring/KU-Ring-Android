package com.ku_stacks.ku_ring.viewmodel_usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.MockUtil
import com.ku_stacks.ku_ring.MockUtil.mock
import com.ku_stacks.ku_ring.MockUtil.mockPushEntity
import com.ku_stacks.ku_ring.data.mapper.toPushList
import com.ku_stacks.ku_ring.data.mapper.toPushUiModelList
import com.ku_stacks.ku_ring.getOrAwaitValue
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.PushRepository
import com.ku_stacks.ku_ring.ui.my_notification.NotificationViewModel
import com.ku_stacks.ku_ring.util.PreferenceUtil
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.junit.Assert.assertEquals
import org.mockito.Mockito

class NotificationViewModelTest {

    private lateinit var viewModel: NotificationViewModel
    private val pushRepository: PushRepository = mock()
    private val noticeRepository: NoticeRepository = mock()
    private val pref: PreferenceUtil = mock()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = NotificationViewModel(pushRepository, noticeRepository, pref)
    }

    @Test
    fun `get MyNotification Test`() {
        // given
        val mockData = listOf(MockUtil.mockPushEntity()).toPushList()
        Mockito.`when`(pushRepository.getMyNotification()).thenReturn(Flowable.just(mockData))

        // when
        viewModel.getMyNotification()
        viewModel.pushUiModelList.getOrAwaitValue()

        // then
        val expected = mockData.toPushUiModelList()
        verify(pushRepository, atLeastOnce()).getMyNotification()
        assertEquals(expected, viewModel.pushUiModelList.value)
    }

    @Test
    fun `updateNotification As Old Test`() {
        // given
        val mockData = mockPushEntity()
        Mockito.`when`(pushRepository.updateNotificationAsOld(mockData.articleId)).thenReturn(Completable.complete())

        // when
        viewModel.updateNotificationToBeOld(mockData.articleId)

        // then
        verify(pushRepository, times(1)).updateNotificationAsOld(mockData.articleId)
    }
}