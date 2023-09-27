package com.ku_stacks.ku_ring.viewmodel_usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.MockUtil.mock
import com.ku_stacks.ku_ring.data.mapper.toPushUiModelList
import com.ku_stacks.ku_ring.getOrAwaitValue
import com.ku_stacks.ku_ring.local.entity.PushEntity
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.push.mapper.toPushList
import com.ku_stacks.ku_ring.push.repository.PushRepository
import com.ku_stacks.ku_ring.ui.my_notification.NotificationViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class NotificationViewModelTest {

    private lateinit var viewModel: NotificationViewModel
    private val pushRepository: PushRepository = mock()
    private val pref: PreferenceUtil = mock()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = NotificationViewModel(pushRepository, pref)
    }

    @Test
    fun `get MyNotification List Test`() {
        // given
        val mockData = listOf(PushEntity.mock()).toPushList()
        Mockito.`when`(pushRepository.getMyNotificationList()).thenReturn(Flowable.just(mockData))

        // when
        viewModel.getMyNotificationList()
        viewModel.pushUiModelList.getOrAwaitValue()

        // then
        val expected = mockData.toPushUiModelList()
        verify(pushRepository, atLeastOnce()).getMyNotificationList()
        assertEquals(expected, viewModel.pushUiModelList.value)
    }

    @Test
    fun `updateNotification As Old Test`() {
        // given
        val mockData = PushEntity.mock()
        Mockito.`when`(pushRepository.updateNotificationAsOld(mockData.articleId))
            .thenReturn(Completable.complete())

        // when
        viewModel.updateNotificationToBeOld(mockData.articleId)

        // then
        verify(pushRepository, times(1)).updateNotificationAsOld(mockData.articleId)
    }
}