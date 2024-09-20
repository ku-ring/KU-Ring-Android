package com.ku_stacks.ku_ring.notice.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.ku_stacks.ku_ring.notice.source.NoticePagingSource
import com.ku_stacks.ku_ring.notice.test.NoticeTestUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class NoticePagingSourceTest {

    private val client: NoticeClient = Mockito.mock(NoticeClient::class.java)
    private lateinit var noticePagingSource: NoticePagingSource

    @get:Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        noticePagingSource = NoticePagingSource(
            "bch",
            client
        )
    }

    @Test
    suspend fun `load PagingSource Refresh Success Test`() {
        // given
        val mockData = NoticeTestUtil.fakeNoticeListResponse()
        Mockito.`when`(
            client.fetchNoticeList(
                "bch",
                0,
                20
            )
        ).thenReturn(mockData)

        // when, then
        val refreshRequest: LoadParams.Refresh<Int> = LoadParams.Refresh(
            null,
            1,
            true
        )
        val result = noticePagingSource.load(refreshRequest)
        assert(result is LoadResult.Page)
        assert((result as LoadResult.Page).data.size == 1)
        assert(result.data.first() == NoticeTestUtil.fakeNotice())
        assert(result.prevKey == null)
        assert(result.nextKey == 20)

        Mockito.verify(
            client,
            Mockito.atLeastOnce()
        ).fetchNoticeList(
            "bch",
            0,
            20
        )
    }

    @Test
    suspend fun `load PagingSource Append Success Test`() {
        // given
        val mockData = NoticeTestUtil.fakeNoticeListResponse()
        Mockito.`when`(
            client.fetchNoticeList(
                "bch",
                20,
                20
            )
        ).thenReturn(mockData)

        // when, then
        val appendRequest: LoadParams.Append<Int> = LoadParams.Append(
            20,
            2,
            true
        )
        val result = noticePagingSource.load(appendRequest)
        assert(result is LoadResult.Page)
        assert((result as LoadResult.Page).data.size == 1)
        assert(result.data.first() == NoticeTestUtil.fakeNotice())
        assert(result.prevKey == 0)
        assert(result.nextKey == 40)

        Mockito.verify(
            client,
            Mockito.atLeastOnce()
        ).fetchNoticeList(
            "bch",
            20,
            20
        )
    }
}