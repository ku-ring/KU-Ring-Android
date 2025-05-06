package com.ku_stacks.ku_ring.domain.noticecomment

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.ku_stacks.ku_ring.domain.noticecomment.repository.NoticeCommentRepository
import com.ku_stacks.ku_ring.domain.noticecomment.usecase.GetNoticeCommentPagingSource
import com.ku_stacks.ku_ring.domain.noticecomment.util.NoticeCommentFactory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class GetNoticeCommentPagingSourceTest {

    private val noticeCommentRepository: NoticeCommentRepository =
        Mockito.mock(NoticeCommentRepository::class.java)

    @Test
    fun loadSinglePageTest() = runTest {
        val noticeCommentPagingSource =
            GetNoticeCommentPagingSource(noticeCommentRepository, NoticeCommentFactory.NOTICE_ID)
        val factory = NoticeCommentFactory()
        val comments = List(40) { factory.create() }

        // given
        Mockito.`when`(noticeCommentRepository.getComment(NoticeCommentFactory.NOTICE_ID, 1))
            .thenReturn(Result.success(Pair(comments.subList(0, 20), true)))
        Mockito.`when`(noticeCommentRepository.getComment(NoticeCommentFactory.NOTICE_ID, 21))
            .thenReturn(Result.success(Pair(comments.subList(20, 40), false)))

        // when
        val pager = TestPager(PagingConfig(20), noticeCommentPagingSource)

        // then
        val firstResult = pager.refresh() as PagingSource.LoadResult.Page
        assertEquals(firstResult.data, comments.subList(0, 20))

        val secondResult = pager.append() as PagingSource.LoadResult.Page
        assertEquals(secondResult.data, comments.subList(20, 40))
    }
}