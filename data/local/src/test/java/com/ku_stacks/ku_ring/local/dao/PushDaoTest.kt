package com.ku_stacks.ku_ring.local.dao

import com.ku_stacks.ku_ring.local.LocalDbAbstract
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.local.test.LocalTestUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
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
        val pushFromDB = pushDao.getNotificationList().first().first()

        // then
        assertThat(pushFromDB.articleId, `is`(pushMock.articleId))
        assertThat(pushFromDB.category, `is`(pushMock.category))
        assertThat(pushFromDB.postedDate, `is`(pushMock.postedDate))
        assertThat(pushFromDB.subject, `is`(pushMock.subject))
        assertThat(pushFromDB.fullUrl, `is`(pushMock.fullUrl))
        assertThat(pushFromDB.isNew, `is`(pushMock.isNew))
        assertThat(pushFromDB.receivedDate, `is`(pushMock.receivedDate))
    }

    @Test
    fun `update Notification As Old Test`() = runBlocking {
        // given
        val pushMock = LocalTestUtil.fakePushEntity()
        pushDao.insertNotification(pushMock)
        pushDao.updateNotificationAsOld(pushMock.articleId, false)

        // when
        val pushFromDB = pushDao.getNotificationList().first().first()

        // then : updateConfirmedNotification 하면 isNew 값이 false
        assertThat(pushFromDB.articleId, `is`(pushMock.articleId))
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
        pushDao.updateNotificationAsOld(pushMock.articleId, false)
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
        pushDao.deleteNotification(pushMock.articleId)
        // then : delete 후에 0개의 데이터
        notiCountFromDB = pushDao.getNotificationCount(true).first()
        assertThat(notiCountFromDB, `is`(0))
    }
}
