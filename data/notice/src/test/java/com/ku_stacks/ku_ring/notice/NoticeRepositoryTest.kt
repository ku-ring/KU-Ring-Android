package com.ku_stacks.ku_ring.notice

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.mapper.toNotice
import com.ku_stacks.ku_ring.notice.repository.NoticeRepository
import com.ku_stacks.ku_ring.notice.repository.NoticeRepositoryImpl
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.remote.notice.response.SubscribeListResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
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
        repository = NoticeRepositoryImpl(client, dao, pref, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insert Notice As Old Test`() {
        // given
        val mockData = NoticeEntity.mock()
        Mockito.`when`(dao.insertNoticeAsOld(mockData)).thenReturn(Completable.complete())

        // when + then
        repository.insertNoticeAsOld(mockData.toNotice())
            .test()
            .assertComplete()
    }

    @Test
    fun `updateNotice Test`() {
        // given
        val mockData = NoticeEntity.mockRead()
        Mockito.`when`(dao.updateNoticeAsRead(mockData.articleId, mockData.category))
            .thenReturn(Completable.complete())

        // when + then
        repository.updateNoticeToBeRead(mockData.articleId, mockData.category)
            .test()
            .assertComplete()
    }

    @Test
    fun `fetch Subscription From Remote Test`() {
        // given
        val mockToken =
            "AAAAn6eQM_Y:APA91bES4rjrFwPY5i_Hz-kT0u32SzIUxreYm9qaQHZeYKGGV_BmHZNJhHvlDjyQA6LveNdxCVrwzsq78jgsnCw8OumbtM5L3cc17XgdqZ_dlpsPzR7TlJwBFTXRFLPst663IeX27sb0"
        val mockSubscribeList = SubscribeListResponse.mock()

        Mockito.`when`(client.fetchSubscribe(mockToken)).thenReturn(Single.just(mockSubscribeList))
        val expected = mockSubscribeList.categoryList.map { it.koreanName }

        // when + then
        repository.fetchSubscriptionFromRemote(mockToken)
            .test()
            .assertNoErrors()
            .assertValue(expected)

        Mockito.verify(client, Mockito.atLeastOnce()).fetchSubscribe(mockToken)
    }

    @Test
    fun `save Subscription To Remote Test`() {
        // given
        val mockRequest = SubscribeRequest.mock()
        val mockResponse = DefaultResponse.mock()
        val mockToken = "mockToken"

        Mockito.`when`(client.saveSubscribe(mockToken, mockRequest))
            .thenReturn(Single.just(mockResponse))

        // when
        repository.saveSubscriptionToRemote(mockToken, mockRequest)

        // then
        Mockito.verify(client, times(1)).saveSubscribe(mockToken, mockRequest)
        Assert.assertEquals(false, pref.firstRunFlag)
    }
}