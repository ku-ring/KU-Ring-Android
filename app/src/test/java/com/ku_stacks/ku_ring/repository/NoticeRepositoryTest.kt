package com.ku_stacks.ku_ring.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ku_stacks.ku_ring.MockUtil.mockNoticeEntity
import com.ku_stacks.ku_ring.MockUtil.mockReadNoticeEntity
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.util.PreferenceUtil
import io.reactivex.rxjava3.core.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class NoticeRepositoryTest {

    private lateinit var repository: NoticeRepository
    private val client: NoticeClient = Mockito.mock(NoticeClient::class.java)
    private val dao: NoticeDao = Mockito.mock(NoticeDao::class.java)
    private val pref: PreferenceUtil = Mockito.mock(PreferenceUtil::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repository = NoticeRepositoryImpl(client, dao, pref)
    }

    @Test
    fun `insert Notice As Old Test`() {
        // given
        val mockData = mockNoticeEntity()
        Mockito.`when`(dao.insertNoticeAsOld(mockData)).thenReturn(Completable.complete())

        // when + then
        repository.insertNoticeAsOld(mockData.articleId, mockData.category)
            .test()
            .assertComplete()
    }

    @Test
    fun `updateNotice Test`() {
        // given
        val mockData = mockReadNoticeEntity()
        Mockito.`when`(dao.updateNoticeAsRead(mockData.articleId)).thenReturn(Completable.complete())

        // when + then
        repository.updateNoticeToBeRead(mockData.articleId)
            .test()
            .assertComplete()
    }
}