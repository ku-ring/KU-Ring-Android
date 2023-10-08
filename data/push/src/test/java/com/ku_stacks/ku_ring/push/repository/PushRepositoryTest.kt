package com.ku_stacks.ku_ring.push.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.local.LocalFixtures
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.push.mapper.toPushList
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class PushRepositoryTest {

    private lateinit var repository: PushRepository
    private val dao: PushDao = Mockito.mock(PushDao::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repository = PushRepositoryImpl(dao)
    }

    @Test
    fun `get MyNotification List Test`() {
        // given
        val mockData = listOf(LocalFixtures.pushEntity())
        Mockito.`when`(dao.getNotificationList()).thenReturn(Flowable.just(mockData))

        val expectedData = mockData.toPushList()
        // when + then
        repository.getMyNotificationList()
            .test()
            .assertNoErrors()
            .assertValue(expectedData)
    }

    @Test
    fun `update Notification As Old Test`() {
        // given
        val mockData = LocalFixtures.pushEntity()
        Mockito.`when`(dao.updateNotificationAsOld(mockData.articleId, false))
            .thenReturn(Completable.complete())

        // when + then
        repository.updateNotificationAsOld(mockData.articleId)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `get Notification Count Test`() {
        // given
        val count = 4
        Mockito.`when`(dao.getNotificationCount(true)).thenReturn(Flowable.just(count))

        // when + then
        repository.getNotificationCount()
            .test()
            .assertNoErrors()
            .assertValue(count)
    }

    @Test
    fun `delete notification Test`() {
        //given
        val mockData = LocalFixtures.pushEntity()
        Mockito.`when`(dao.deleteNotification(mockData.articleId))
            .thenReturn(Completable.complete())

        // when + then
        dao.deleteNotification(mockData.articleId)
            .test()
            .assertNoErrors()
            .assertComplete()
    }
}