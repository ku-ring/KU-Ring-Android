package com.ku_stacks.ku_ring.paging_source

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ku_stacks.ku_ring.MockUtil
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.source.DepartmentNoticeMediator
import com.ku_stacks.ku_ring.LocalDbAbstract
import com.ku_stacks.ku_ring.util.PreferenceUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [24])
class DepartmentNoticeMediatorTest : LocalDbAbstract() {

    private val client: NoticeClient = Mockito.mock(NoticeClient::class.java)
    private val noticeDao: NoticeDao = Mockito.mock(NoticeDao::class.java)
    private lateinit var mediator: DepartmentNoticeMediator
    private lateinit var preferenceUtil: PreferenceUtil

    @get:Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    private val shortName = "cse"
    private val pageSize = 20

    @Before
    fun setUp() {
        val applicationContext: Context = getApplicationContext()
        preferenceUtil = PreferenceUtil(applicationContext)
        mediator = DepartmentNoticeMediator(
            shortName = shortName,
            noticeClient = client,
            noticeDao = noticeDao,
            preferences = preferenceUtil,
        )
    }

    @Test
    @OptIn(ExperimentalPagingApi::class)
    fun `noticeClient succeeds and returns some notices`() = runTest {
        // given
        val mockResponse = MockUtil.mockSucceededDepartmentNoticeListResponse(pageSize)
        Mockito.`when`(
            client.fetchDepartmentNoticeList(
                type = "dep",
                shortName = shortName,
                page = 0,
                size = pageSize
            )
        ).thenReturn(mockResponse)

        // when
        val pagingState = PagingState<Int, NoticeEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize),
            leadingPlaceholderCount = pageSize,
        )
        val result = mediator.load(loadType = LoadType.REFRESH, state = pagingState)

        // then
        assert(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    @OptIn(ExperimentalPagingApi::class)
    fun `noticeClient succeeds but returns empty list`() = runTest {
        // given
        val mockResponse = MockUtil.mockEmptyDepartmentNoticeListResponse()
        Mockito.`when`(
            client.fetchDepartmentNoticeList(
                shortName = shortName,
                page = 0,
                size = pageSize
            )
        ).thenReturn(mockResponse)

        // when
        val pagingState = PagingState<Int, NoticeEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize),
            leadingPlaceholderCount = pageSize,
        )
        val result = mediator.load(loadType = LoadType.REFRESH, state = pagingState)

        // then
        assert(result is RemoteMediator.MediatorResult.Success)
        assert((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    @OptIn(ExperimentalPagingApi::class)
    fun `noticeClient fails and returns error`() = runTest {
        // given
        Mockito.`when`(
            client.fetchDepartmentNoticeList(
                type = "dep",
                shortName = shortName,
                page = 0,
                size = pageSize
            )
        ).thenThrow(IllegalStateException())

        // when
        val pagingState = PagingState<Int, NoticeEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize),
            leadingPlaceholderCount = pageSize,
        )
        val result = mediator.load(loadType = LoadType.REFRESH, state = pagingState)

        // then
        assert(result is RemoteMediator.MediatorResult.Error)
    }

}