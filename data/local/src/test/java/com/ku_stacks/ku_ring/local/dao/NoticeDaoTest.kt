package com.ku_stacks.ku_ring.local.dao

import androidx.paging.PagingSource
import com.ku_stacks.ku_ring.local.LocalDbAbstract
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.local.test.LocalTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [24])
class NoticeDaoTest : LocalDbAbstract() {

    private lateinit var noticeDao: NoticeDao

    @Before
    fun init() {
        noticeDao = db.noticeDao()
    }

    private fun readNoticeMock(): NoticeEntity {
        return LocalTestUtil.fakeNoticeEntity().copy(isNew = true, isRead = true)
    }

    @Test
    fun `insertNoticeAsOld and getOldNotice Test`() = runTest {
        // given
        val noticeMock = LocalTestUtil.fakeNoticeEntity()
        noticeDao.insertNoticeAsOld(noticeMock)

        // when
        val noticeFromDB = noticeDao.getOldNoticeList().first().first()

        // then : 생성하고 insert 한 mock 와 불러온 데이터가 일치
        assertThat(noticeMock, `is`(noticeFromDB))
    }

    @Test
    fun `updateNoticeAsOld and getOldNoticeList Test`() = runTest {
        // given
        val noticeMock = LocalTestUtil.fakeNoticeEntity()
        noticeDao.insertNoticeAsOld(noticeMock)

        // when
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock)

        // then : insert 후에 update 해도 데이터 수는 1개, 불러온 데이터가 mock 와 일치
        val noticeFromDB = noticeDao.getOldNoticeList().first()
        assertThat(noticeFromDB.size, `is`(1))
        assertThat(noticeFromDB[0].toString(), `is`(readNoticeMock.toString()))
    }

    @Test
    fun `updateNotice and getCountOfReadNoticeTest`() = runTest {
        // given
        val noticeMock = LocalTestUtil.fakeNoticeEntity()
        noticeDao.insertNoticeAsOld(noticeMock)

        // when
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock)

        // then : insert 후에 isRead 를 true 로 update 하면 읽은 공지 데이터가 1개
        val countForReadNotice =
            noticeDao.getCountOfReadNotice(true, noticeMock.articleId)
        assertThat(countForReadNotice, `is`(1))
    }

    @Test
    fun `updateNotice and isReadNotice Test`() = runTest {
        // given
        val noticeMock = LocalTestUtil.fakeNoticeEntity()
        noticeDao.insertNoticeAsOld(noticeMock)

        // when
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock)

        // then : isRead 를 true 로 update 한 공지는 불러왔을 때 isRead 값이 true
        assertThat(noticeDao.isReadNotice(noticeMock.articleId), `is`(true))
    }

    @Test
    fun `updateNotice and getReadNoticeList Test`() = runTest {
        // given
        val noticeMock = LocalTestUtil.fakeNoticeEntity()
        noticeDao.insertNoticeAsOld(noticeMock)

        // when
        val sizeOfNotReadNotice = noticeDao.getReadNoticeList(true).first().size
        // then : 공지를 읽기 전엔 isRead 가 true 인 데이터 0개
        assertThat(sizeOfNotReadNotice, `is`(0))

        // given
        val readNoticeMock = readNoticeMock()
        noticeDao.updateNotice(readNoticeMock)

        // when
        val sizeOfReadNotice = noticeDao.getReadNoticeList(true).first().size
        // then : 공지를 읽은 후엔 isRead 가 true 인 데이터 1개
        assertThat(sizeOfReadNotice, `is`(1))
    }

    @Test
    fun `updateNoticeSaveState and getSavedNotices Test`() = runTest {
        // given
        val notice = LocalTestUtil.fakeNoticeEntity()
        noticeDao.insertNoticeAsOld(notice)

        // when
        noticeDao.updateNoticeSaveState(notice.articleId, notice.category, true)

        // then
        val savedNotice = notice.copy(isSaved = true)
        val noticeFromDB = noticeDao.getNoticesBySaved(true).first().first()
        assertThat(noticeFromDB, `is`(savedNotice))
    }

    @Test
    fun `updateNoticeAsReadOnStorage and getSavedNotices Test`() = runTest {
        // given
        val notice = LocalTestUtil.fakeDepartmentNoticeEntity()
        noticeDao.insertNoticeAsOld(notice)

        // when
        noticeDao.updateNoticeSaveState(notice.articleId, notice.category, true)
        noticeDao.updateNoticeAsReadOnStorage(notice.articleId, notice.category, true)

        // then
        val savedAndReadNotice = notice.copy(isSaved = true, isReadOnStorage = true)
        val noticeFromDB = noticeDao.getNoticesBySaved(true).first()[0]
        assertThat(noticeFromDB, `is`(savedAndReadNotice))
    }

    @Test
    fun `insertDepartmentNotices and getDepartmentNotices Test`() = runTest {
        // given
        val departmentNotice = LocalTestUtil.fakeDepartmentNoticeEntity()
        noticeDao.insertDepartmentNotices(listOf(departmentNotice))

        // when
        noticeDao.updateNoticeAsRead(
            articleId = departmentNotice.articleId,
            category = departmentNotice.category
        )

        // then
        val departmentNoticePage = noticeDao.getDepartmentNotices(departmentNotice.department)
            .load(PagingSource.LoadParams.Append(0, 20, false))
        assert(departmentNoticePage is PagingSource.LoadResult.Page)

        val departmentNotices = (departmentNoticePage as PagingSource.LoadResult.Page).data
        assertEquals(1, departmentNotices.size)
        assert(departmentNotices[0].isRead)
        assertEquals(departmentNotice.copy(isRead = true), departmentNotices[0])
    }

    @Test
    fun `insertDepartmentNotices and clearDepartment Test`() = runTest {
        // given
        val departmentNoticeMocks = (1..10).map {
            LocalTestUtil.fakeDepartmentNoticeEntity().copy(articleId = it.toString())
        }
        noticeDao.insertDepartmentNotices(departmentNoticeMocks)

        // when
        val department = departmentNoticeMocks[0].department
        noticeDao.clearDepartment(department)

        // then
        val departmentNoticePage = noticeDao.getDepartmentNotices(department)
            .load(PagingSource.LoadParams.Append(0, 20, false))
        assert(departmentNoticePage is PagingSource.LoadResult.Page)

        val departmentNotices = (departmentNoticePage as PagingSource.LoadResult.Page).data
        assert(departmentNotices.isEmpty())
    }
}