package com.ku_stacks.ku_ring.persistence

import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class NoticeDaoTest : LocalDbAbstract() {

    private lateinit var noticeDao: NoticeDao

    @Before
    fun init() {
        noticeDao = db.noticeDao()
    }

    private fun noticeMock() = NoticeEntity(
        articleId = "5b4a11b",
        category = "bachelor",
        subject = "2023학년도 전과 선발자 안내",
        postedDate = "20230208",
        url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
        isNew = false,
        isRead = false,
        isSaved = false,
        isReadOnStorage = false,
    )

    private fun readNoticeMock(): NoticeEntity {
        return noticeMock().copy(isNew = true, isRead = true)
    }

    @Test
    fun `insertNoticeAsOld and getOldNotice Test`() {
        // given
        val noticeMock = noticeMock()
        noticeDao.insertNoticeAsOld(noticeMock).blockingSubscribe()

        // when
        val noticeFromDB = noticeDao.getOldNoticeList().blockingGet()[0]

        // then : 생성하고 insert 한 mock 와 불러온 데이터가 일치
        assertThat(noticeMock, `is`(noticeFromDB))
    }

    @Test
    fun `updateNoticeAsOld and getOldNoticeList Test`() {
        // given
        val noticeMock = noticeMock()
        noticeDao.insertNoticeAsOld(noticeMock).blockingSubscribe()

        // when
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        // then : insert 후에 update 해도 데이터 수는 1개, 불러온 데이터가 mock 와 일치
        val noticeFromDB = noticeDao.getOldNoticeList().blockingGet()
        assertThat(noticeFromDB.size, `is`(1))
        assertThat(noticeFromDB[0].toString(), `is`(readNoticeMock.toString()))
    }

    @Test
    fun `updateNotice and getCountOfReadNoticeTest`() {
        // given
        val noticeMock = noticeMock()
        noticeDao.insertNoticeAsOld(noticeMock).blockingSubscribe()

        // when
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        // then : insert 후에 isRead 를 true 로 update 하면 읽은 공지 데이터가 1개
        val countForReadNotice =
            noticeDao.getCountOfReadNotice(true, noticeMock.articleId).blockingGet()
        assertThat(countForReadNotice, `is`(1))
    }

    @Test
    fun `updateNotice and isReadNotice Test`() {
        // given
        val noticeMock = noticeMock()
        noticeDao.insertNoticeAsOld(noticeMock).blockingSubscribe()

        // when
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        // then : isRead 를 true 로 update 한 공지는 불러왔을 때 isRead 값이 true
        assertThat(noticeDao.isReadNotice(noticeMock.articleId), `is`(true))
    }

    @Test
    fun `updateNotice and getReadNoticeList Test`() {
        // given
        val noticeMock = noticeMock()
        noticeDao.insertNoticeAsOld(noticeMock).blockingSubscribe()

        // when
        val sizeOfNotReadNotice = noticeDao.getReadNoticeList(true).blockingFirst().size
        // then : 공지를 읽기 전엔 isRead 가 true 인 데이터 0개
        assertThat(sizeOfNotReadNotice, `is`(0))

        // given
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock).blockingSubscribe()

        // when
        val sizeOfReadNotice = noticeDao.getReadNoticeList(true).blockingFirst().size
        // then : 공지를 읽은 후엔 isRead 가 true 인 데이터 1개
        assertThat(sizeOfReadNotice, `is`(1))
    }

    @Test
    fun `updateNoticeSaveState and getSavedNotices Test`() = runTest {
        // given
        val notice = noticeMock()
        noticeDao.insertNoticeAsOld(notice).blockingSubscribe()

        // when
        noticeDao.updateNoticeSaveState(notice.articleId, true)

        // then
        val savedNotice = notice.copy(isSaved = true)
        val noticeFromDB = noticeDao.getNoticesBySaved(true).first()[0]
        assertThat(noticeFromDB, `is`(savedNotice))
    }

    @Test
    fun `updateNoticeAsReadOnStorage and getSavedNotices Test`() = runTest {
        // given
        val notice = noticeMock()
        noticeDao.insertNoticeAsOld(notice).blockingSubscribe()

        // when
        noticeDao.updateNoticeSaveState(notice.articleId, true)
        noticeDao.updateNoticeAsReadOnStorage(notice.articleId, true)

        // then
        val savedAndReadNotice = notice.copy(isSaved = true, isReadOnStorage = true)
        val noticeFromDB = noticeDao.getNoticesBySaved(true).first()[0]
        assertThat(noticeFromDB, `is`(savedAndReadNotice))
    }
}