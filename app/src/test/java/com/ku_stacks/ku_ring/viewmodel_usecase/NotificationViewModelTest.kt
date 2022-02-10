package com.ku_stacks.ku_ring.viewmodel_usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.MockUtil
import com.ku_stacks.ku_ring.MockUtil.mock
import com.ku_stacks.ku_ring.data.mapper.toPushList
import com.ku_stacks.ku_ring.getOrAwaitValue
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.NoticeRepositoryImpl
import com.ku_stacks.ku_ring.repository.PushRepository
import com.ku_stacks.ku_ring.repository.PushRepositoryImpl
import com.ku_stacks.ku_ring.ui.my_notification.NotificationViewModel
import com.ku_stacks.ku_ring.util.PreferenceUtil
import io.reactivex.rxjava3.core.Flowable
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify

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
        viewModel.pushList.getOrAwaitValue()

        // then
        verify(pushRepository, atLeastOnce()).getMyNotification()
        assertEquals(viewModel.pushList.value, mockData)
    }
}