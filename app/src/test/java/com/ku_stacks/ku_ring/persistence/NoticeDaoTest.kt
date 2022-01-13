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

    private fun noticeMock() = NoticeEntity(
        articleId = "ababab",
        category = "bachelor",
        isNew = false,
        isRead = false
    )

    private fun readNoticeMock(): NoticeEntity {
        val noticeMock = noticeMock()
        return NoticeEntity(
            articleId = noticeMock.articleId,
            category = noticeMock.category,
            isNew = true,
            isRead = true
        )
    }

    @Test
    fun insertAndGetNoticeTest() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()
        val noticeFromDB = noticeDao.getNoticeRecord().blockingGet()[0]
        assertThat(noticeMock, `is`(noticeFromDB))
    }

    @Test
    fun updateNoticeAndGetNoticeRecordTest() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()
        assertThat(noticeDao.getNoticeRecord().blockingGet().size, `is`(1))

        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        val noticeFromDB = noticeDao.getNoticeRecord().blockingGet()
        assertThat(noticeFromDB.size, `is`(1))
        assertThat(noticeFromDB[0].toString(), `is`(readNoticeMock.toString()))
    }

    @Test
    fun updateNoticeAndGetCountForReadNoticeTest() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()
        assertThat(noticeDao.getNoticeRecord().blockingGet().size, `is`(1))

        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        val countForReadNotice =
            noticeDao.getCountForReadNotice(true, noticeMock.articleId)
                .blockingGet()
        assertThat(countForReadNotice, `is`(1))
    }

    @Test
    fun updateNoticeAndIsReadNoticeTest() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()
        assertThat(noticeDao.getNoticeRecord().blockingGet().size, `is`(1))

        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()
        assertThat(noticeDao.isReadNotice(noticeMock.articleId), `is`(true))
    }

    @Test
    fun updateNoticeAndGetReadNoticeRecordTest() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()
        assertThat(noticeDao.getNoticeRecord().blockingGet().size, `is`(1))

        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()
        assertThat(noticeDao.getReadNoticeRecord(true).blockingFirst().size, `is`(1))
    }
}