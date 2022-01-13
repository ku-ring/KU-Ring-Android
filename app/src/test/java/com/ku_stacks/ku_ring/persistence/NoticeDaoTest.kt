package com.ku_stacks.ku_ring.persistence

import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class NoticeDaoTest : LocalDbAbstract() {

    private lateinit var noticeDao: NoticeDao

    @Before
    fun init() {
        noticeDao = db.noticeDao()
    }

    @Test
    fun insertAndLoadNoticeTest() {
        val noticeMock = NoticeEntity(
            articleId = "ababab",
            category = "bachelor",
            isNew = false,
            isRead = false
        )
        noticeDao.insertNotice(noticeMock).blockingSubscribe()
        val noticeFromDB = noticeDao.getNoticeRecord().blockingGet()[0]
        assertThat(noticeMock, `is`(noticeFromDB))
    }
}