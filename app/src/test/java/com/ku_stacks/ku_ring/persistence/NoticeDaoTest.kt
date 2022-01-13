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
    fun `insertAndGetNotice Test`() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()
        val noticeFromDB = noticeDao.getNoticeRecord().blockingGet()[0]
        // 생성하고 insert 한 mock 와 불러온 데이터가 일치
        assertThat(noticeMock, `is`(noticeFromDB))
    }

    @Test
    fun `updateNotice and getNoticeRecord Test`() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()

        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        // insert 후에 update 해도 데이터 수는 1개, 불러온 데이터가 mock 와 일치
        val noticeFromDB = noticeDao.getNoticeRecord().blockingGet()
        assertThat(noticeFromDB.size, `is`(1))
        assertThat(noticeFromDB[0].toString(), `is`(readNoticeMock.toString()))
    }

    @Test
    fun `updateNotice and getCountForReadNoticeTest`() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()

        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        // insert 후에 isRead 를 true 로 update 하면 읽은 공지 데이터가 1개
        val countForReadNotice =
            noticeDao.getCountForReadNotice(true, noticeMock.articleId).blockingGet()
        assertThat(countForReadNotice, `is`(1))
    }

    @Test
    fun `updateNotice and isReadNotice Test`() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()

        // isRead 를 true 로 update 한 공지는 불러왔을 때 isRead 값이 true
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()
        assertThat(noticeDao.isReadNotice(noticeMock.articleId), `is`(true))
    }

    @Test
    fun `updateNotice  getReadNoticeRecord Test`() {
        val noticeMock = noticeMock()
        noticeDao.insertNotice(noticeMock).blockingSubscribe()

        // 공지를 읽기 전엔 isRead 가 true 인 데이터 0개
        val sizeOfNotReadNotice = noticeDao.getReadNoticeRecord(true).blockingFirst().size
        assertThat(sizeOfNotReadNotice, `is`(0))

        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        // 공지를 읽은 후엔 isRead 가 true 인 데이터 1개
        val sizeOfReadNotice = noticeDao.getReadNoticeRecord(true).blockingFirst().size
        assertThat(sizeOfReadNotice, `is`(1))
    }
}