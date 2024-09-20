package com.ku_stacks.ku_ring.notice

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.mapper.toNotice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.notice.repository.NoticeRepositoryImpl
import com.ku_stacks.ku_ring.notice.test.NoticeTestUtil
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.times

@OptIn(ExperimentalCoroutinesApi::class)
class NoticeRepositoryTest {

    private lateinit var repository: NoticeRepository
    private val client: NoticeClient = Mockito.mock(NoticeClient::class.java)
    private val dao: NoticeDao = Mockito.mock(NoticeDao::class.java)
    private val pref: PreferenceUtil = Mockito.mock(PreferenceUtil::class.java)

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = NoticeRepositoryImpl(
            client,
            dao,
            pref,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insert Notice As Old Test`() = runTest {
        // given
        val mockData = NoticeTestUtil.fakeNoticeEntity()
        dao.insertNoticeAsOld(mockData)

        // when + then
        repository.insertNoticeAsOld(mockData.toNotice())
    }

    @Test
    fun `updateNotice Test`() = runTest {
        // given
        val mockData = NoticeTestUtil.fakeNoticeEntity().copy(isRead = true)
        dao.updateNoticeAsRead(
            mockData.articleId,
            mockData.category
        )


        // when + then
        repository.updateNoticeToBeRead(
            mockData.articleId,
            mockData.category
        )
    }

    @Test
    fun `fetch Subscription From Remote Test`() = runTest {
        // given
        val mockToken =
            "AAAAn6eQM_Y:APA91bES4rjrFwPY5i_Hz-kT0u32SzIUxreYm9qaQHZeYKGGV_BmHZNJhHvlDjyQA6LveNdxCVrwzsq78jgsnCw8OumbtM5L3cc17XgdqZ_dlpsPzR7TlJwBFTXRFLPst663IeX27sb0"
        val mockSubscribeList = NoticeTestUtil.fakeSubscribeListResponse()

        Mockito.`when`(client.fetchSubscribe(mockToken)).thenReturn(mockSubscribeList)
        val expected = mockSubscribeList.categoryList.map { it.koreanName }

        // when + then
        val result = repository.fetchSubscriptionFromRemote(mockToken)
        assert(result == expected)

        Mockito.verify(
            client,
            Mockito.atLeastOnce()
        ).fetchSubscribe(mockToken)
    }

    @Test
    fun `save Subscription To Remote Test`() = runTest {
        // given
        val request = NoticeTestUtil.fakeSubscribeRequest()
        val response = NoticeTestUtil.fakeDefaultResponse()
        val token = "mockToken"

        Mockito.`when`(
            client.saveSubscribe(
                token,
                request
            )
        ).thenReturn(response)

        // when
        repository.saveSubscriptionToRemote(
            token,
            request.categories
        )

        // then
        Mockito.verify(
            client,
            times(1)
        ).saveSubscribe(
            token,
            request
        )
        assertEquals(
            false,
            pref.firstRunFlag
        )
    }
}