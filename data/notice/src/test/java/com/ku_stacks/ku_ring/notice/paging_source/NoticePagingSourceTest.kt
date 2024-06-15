package com.ku_stacks.ku_ring.notice.paging_source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.ku_stacks.ku_ring.notice.source.NoticePagingSource
import com.ku_stacks.ku_ring.notice.test.NoticeTestUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import io.reactivex.rxjava3.core.Single
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
        noticePagingSource = NoticePagingSource("bch", client)
    }

    @Test
    fun `load PagingSource Refresh Success Test`() {
        // given
        val mockData = NoticeTestUtil.fakeNoticeListResponse()
        Mockito.`when`(client.fetchNoticeList("bch", 0, 20)).thenReturn(Single.just(mockData))

        // when, then
        val refreshRequest: LoadParams.Refresh<Int> = LoadParams.Refresh(null, 1, true)
        noticePagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(
                LoadResult.Page(
                    data = listOf(NoticeTestUtil.fakeNotice()),
                    prevKey = null,
                    nextKey = 20
                )
            )

        Mockito.verify(client, Mockito.atLeastOnce()).fetchNoticeList("bch", 0, 20)
    }

    @Test
    fun `load PagingSource Append Success Test`() {
        // given
        val mockData = NoticeTestUtil.fakeNoticeListResponse()
        Mockito.`when`(client.fetchNoticeList("bch", 20, 20)).thenReturn(Single.just(mockData))

        // when, then
        val appendRequest: LoadParams.Append<Int> = LoadParams.Append(20, 2, true)
        noticePagingSource.loadSingle(appendRequest)
            .test()
            .await()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(
                LoadResult.Page(
                    data = listOf(NoticeTestUtil.fakeNotice()),
                    prevKey = 0,
                    nextKey = 40
                )
            )

        Mockito.verify(client, Mockito.atLeastOnce()).fetchNoticeList("bch", 20, 20)
    }
}