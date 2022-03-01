package com.ku_stacks.ku_ring.viewmodel_usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.MockUtil.mock
import com.ku_stacks.ku_ring.MockUtil.mockNotice
import com.ku_stacks.ku_ring.repository.NoticeRepository
import com.ku_stacks.ku_ring.repository.PushRepository
import com.ku_stacks.ku_ring.ui.home.HomeViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val pushRepository: PushRepository = mock()
    private val noticeRepository: NoticeRepository = mock()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Mockito.`when`(pushRepository.getNotificationCount()).thenReturn(Flowable.just(0))
        viewModel = HomeViewModel(noticeRepository, pushRepository)
    }

    @Test
    fun `update Notice Tobe Read Test`() {
        // given
        val mockData = mockNotice()
        Mockito.`when`(noticeRepository.updateNoticeToBeRead(mockData.articleId, mockData.category))
            .thenReturn(Completable.complete())

        // when
        viewModel.updateNoticeTobeRead(mockData)

        // then
        Mockito.verify(noticeRepository, times(1))
            .updateNoticeToBeRead(mockData.articleId, mockData.category)
    }

    @Test
    fun `insert Notice Test`() {
        // given
        val mockData = mockNotice()
        Mockito.`when`(noticeRepository.insertNotice(mockData.articleId, mockData.category))
            .thenReturn(Completable.complete())

        // when
        viewModel.insertNotice(mockData.articleId, mockData.category)

        // then
        Mockito.verify(noticeRepository, times(1))
            .insertNotice(mockData.articleId, mockData.category)
    }
}