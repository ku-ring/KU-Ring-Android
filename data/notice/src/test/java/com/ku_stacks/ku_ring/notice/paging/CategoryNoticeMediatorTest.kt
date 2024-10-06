package com.ku_stacks.ku_ring.notice.paging

import android.content.Context
import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.room.KuRingDatabase
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.source.CategoryNoticeMediator
import com.ku_stacks.ku_ring.notice.test.NoticeTestUtil
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryNoticeMediatorTest {

    private val noticeClient: NoticeClient = Mockito.mock(NoticeClient::class.java)
    private val noticeDao: NoticeDao = Mockito.mock(NoticeDao::class.java)

    private lateinit var mediator: CategoryNoticeMediator
    private lateinit var preferenceUtil: PreferenceUtil
    private lateinit var db: KuRingDatabase

    private val categoryShortName = "bch"
    private val pageSize = 20

    @Before
    fun setup() {
        val applicationContext: Context = getApplicationContext()
        preferenceUtil = PreferenceUtil(applicationContext)
        initDB()
        mediator = CategoryNoticeMediator(
            categoryShortName,
            noticeClient,
            noticeDao,
            preferenceUtil,
        )
    }

    private fun initDB() {
        db = Room.inMemoryDatabaseBuilder(getApplicationContext(), KuRingDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `noticeClient succeeds and returns some notices`() = runTest {
        // given
        val mockResponse = NoticeTestUtil.fakeNoticeListResponse()
        Mockito.`when`(noticeClient.fetchNoticeList(categoryShortName, 0, pageSize))
            .thenReturn(mockResponse)

        // when
        val result = loadNotice()

        // then
        assert(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `noticeClient succeeds but returns empty list`() = runTest {
        // given
        val mockResponse = NoticeTestUtil.fakeEmptyNoticeListResponse()
        Mockito.`when`(noticeClient.fetchNoticeList(categoryShortName, 0, pageSize))
            .thenReturn(mockResponse)

        // when
        val result = loadNotice()

        // then
        assert(result is RemoteMediator.MediatorResult.Success)
        assert((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `noticeClient fails and returns error`() = runTest {
        // given
        Mockito.`when`(noticeClient.fetchNoticeList(categoryShortName, 0, pageSize))
            .thenThrow(IllegalStateException())

        // when
        val result = loadNotice()

        // then
        assert(result is RemoteMediator.MediatorResult.Error)
    }

    @OptIn(ExperimentalPagingApi::class)
    private suspend fun loadNotice(): RemoteMediator.MediatorResult {
        val pagingState = PagingState<Int, NoticeEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize),
            leadingPlaceholderCount = pageSize,
        )
        return mediator.load(LoadType.REFRESH, pagingState)
    }

}