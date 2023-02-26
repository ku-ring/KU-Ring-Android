package com.ku_stacks.ku_ring.viewmodel_usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.MockUtil.mock
import com.ku_stacks.ku_ring.MockUtil.mockNotice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.PushRepository
import com.ku_stacks.ku_ring.ui.main.notice.NoticeViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times

class HomeViewModelTest {

    private lateinit var viewModel: NoticeViewModel
    private val pushRepository: PushRepository = mock()
    private val noticeRepository: NoticeRepository = mock()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Mockito.`when`(pushRepository.getNotificationCount()).thenReturn(Flowable.just(0))
        viewModel = NoticeViewModel(noticeRepository, pushRepository)
    }

    @Test
    fun `insert Notice As Old Test`() {
        // given
        val mockData = mockNotice()
        Mockito.`when`(noticeRepository.insertNoticeAsOld(mockData))
            .thenReturn(Completable.complete())

        // when
        viewModel.insertNoticeAsOld(mockData)

        // then
        Mockito.verify(noticeRepository, times(1))
            .insertNoticeAsOld(mockData)
    }
}