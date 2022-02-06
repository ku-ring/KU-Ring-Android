package com.ku_stacks.ku_ring.paging_source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.ku_stacks.ku_ring.MockUtil
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.source.NoticePagingSource
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
        val mockData = MockUtil.mockNoticeList()
        Mockito.`when`(client.fetchNoticeList("bch", 0, 20)).thenReturn(Single.just(mockData))

        // when, then
        val refreshRequest: LoadParams.Refresh<Int> = LoadParams.Refresh(null, 1, true)
        noticePagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(LoadResult.Page<Int, Notice>(
                data = listOf(
                    Notice(
                        postedDate = "20220203",
                        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
                        category = "bachelor",
                        url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5b4a11b",
                        articleId = "5b4a11b",
                        isNew = false,
                        isRead = false,
                        isSubscribing = false,
                        tag = emptyList()
                    )
                ),
                prevKey = null,
                nextKey = 20
            ))

        Mockito.verify(client, Mockito.atLeastOnce()).fetchNoticeList("bch", 0, 20)
    }

    @Test
    fun `load PagingSource Append Success Test`() {
        // given
        val mockData = MockUtil.mockNoticeList()
        Mockito.`when`(client.fetchNoticeList("bch", 20, 20)).thenReturn(Single.just(mockData))

        // when, then
        val appendRequest: LoadParams.Append<Int> = LoadParams.Append(20, 2, true)
        noticePagingSource.loadSingle(appendRequest)
            .test()
            .await()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(LoadResult.Page<Int, Notice>(
                data = listOf(
                    Notice(
                        postedDate = "20220203",
                        subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
                        category = "bachelor",
                        url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5b4a11b",
                        articleId = "5b4a11b",
                        isNew = false,
                        isRead = false,
                        isSubscribing = false,
                        tag = emptyList()
                    )
                ),
                prevKey = 0,
                nextKey = 40
            ))

        Mockito.verify(client, Mockito.atLeastOnce()).fetchNoticeList("bch", 20, 20)
    }
}