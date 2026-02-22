package com.ku_stacks.ku_ring.local.dao

import androidx.paging.PagingSource
import com.ku_stacks.ku_ring.local.LocalDbAbstract
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.local.test.LocalTestUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config

@Config(sdk = [24])
class PushDaoTest : LocalDbAbstract() {

    private lateinit var pushDao: PushDao

    @Before
    fun init() {
        pushDao = db.pushDao()
    }

    @Test
    fun `insertNotification and getNotificationList Test`() = runBlocking {
        // given
        val pushMock = LocalTestUtil.fakePushEntity()
        pushDao.insertNotification(pushMock)

        // when
        val pagingSource = pushDao.getNotificationList()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        val resultData = (loadResult as PagingSource.LoadResult.Page).data
        val pushFromDB = resultData.first()

        // then
        assertThat(pushFromDB.content, `is`(pushMock.content))
        assertThat(pushFromDB.isNew, `is`(pushMock.isNew))
        assertThat(pushFromDB.receivedDate, `is`(pushMock.receivedDate))
    }

    @Test
    fun `update Notification As Old Test`() = runBlocking {
        // given
        val pushMock = LocalTestUtil.fakePushEntity()
        pushDao.insertNotification(pushMock)
        pushDao.updateNotificationAsOld(pushMock.id, false)

        // when
        val pagingSource = pushDao.getNotificationList()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )
        val resultData = (loadResult as PagingSource.LoadResult.Page).data
        val pushFromDB = resultData.first()

        // then : updateConfirmedNotification 하면 isNew 값이 false
        assertThat(pushFromDB.id, `is`(pushMock.id))
        assertThat(pushFromDB.isNew, `is`(false))
    }

    @Test
    fun `getNotificationCount Test`() = runBlocking {
        // 공지가 없으면 0개의 데이터임을 확인
        var notiCountFromDB = pushDao.getNotificationCount(true).first()
        assertThat(notiCountFromDB, `is`(0))

        // given
        val pushMock = LocalTestUtil.fakePushEntity()
        pushDao.insertNotification(pushMock)

        // when
        notiCountFromDB = pushDao.getNotificationCount(true).first()
        // then : insert 후에 1개의 데이터
        assertThat(notiCountFromDB, `is`(1))

        // when
        pushDao.updateNotificationAsOld(pushMock.id, false)
        // then : isNew 를 false 로 업데이트하면 0개의 데이터
        notiCountFromDB = pushDao.getNotificationCount(true).first()
        assertThat(notiCountFromDB, `is`(0))
    }

    @Test
    fun `deleteNotification Test`() = runBlocking {
        // 처음엔 0개의 데이터임을 확인
        var notiCountFromDB = pushDao.getNotificationCount(true).first()
        assertThat(notiCountFromDB, `is`(0))

        // when
        val pushMock = LocalTestUtil.fakePushEntity()
        pushDao.insertNotification(pushMock)
        // then : insert 후에 1개의 데이터
        notiCountFromDB = pushDao.getNotificationCount(true).first()
        assertThat(notiCountFromDB, `is`(1))

        // when
        pushDao.deleteNotification(pushMock.id)
        // then : delete 후에 0개의 데이터
        notiCountFromDB = pushDao.getNotificationCount(true).first()
        assertThat(notiCountFromDB, `is`(0))
    }
}
