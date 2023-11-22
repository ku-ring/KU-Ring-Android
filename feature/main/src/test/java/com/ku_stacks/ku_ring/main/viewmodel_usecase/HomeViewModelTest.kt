package com.ku_stacks.ku_ring.main.viewmodel_usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.domain.DomainFixtures
import com.ku_stacks.ku_ring.main.notice.category.NoticesChildViewModel
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.push.repository.PushRepository
import com.ku_stacks.ku_ring.util.MockUtil
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.times

class HomeViewModelTest {

    private lateinit var viewModel: NoticesChildViewModel
    private val pushRepository: PushRepository = MockUtil.mock(PushRepository::class.java)
    private val noticeRepository: NoticeRepository = MockUtil.mock(NoticeRepository::class.java)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Mockito.`when`(pushRepository.getNotificationCount()).thenReturn(Flowable.just(0))
        viewModel = NoticesChildViewModel(noticeRepository)
    }

    @Test
    fun `insert Notice As Old Test`() {
        // given
        val mockData = DomainFixtures.notice()
        Mockito.`when`(noticeRepository.insertNoticeAsOld(mockData))
            .thenReturn(Completable.complete())

        // when
        viewModel.insertNoticeAsOld(mockData)

        // then
        Mockito.verify(noticeRepository, times(1))
            .insertNoticeAsOld(mockData)
    }
}