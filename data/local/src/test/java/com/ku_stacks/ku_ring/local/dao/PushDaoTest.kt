package com.ku_stacks.ku_ring.local.dao

import com.ku_stacks.ku_ring.local.LocalDbAbstract
import com.ku_stacks.ku_ring.local.LocalFixtures
import com.ku_stacks.ku_ring.local.room.PushDao
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
        val pushMock = LocalFixtures.pushEntity()
        pushDao.insertNotification(pushMock)

        // when
        val pushFromDB = pushDao.getNotificationList().blockingFirst()[0]

        // then
        /**
         * 두 데이터를 가져오는 방식이 하나는 coroutine 이고 하나는 RxJava 라서 그런지
         * pushMock.toString() 과 pushFromDB.toString() 의 값이 다르게 보인다.(주솟값)
         * 따라서 객체 내부의 값을 직접 비교하였다.
         */
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
        val pushMock = LocalFixtures.pushEntity()
        pushDao.insertNotification(pushMock)
        pushDao.updateNotificationAsOld(pushMock.articleId, false).blockingSubscribe()

        // when
        val pushFromDB = pushDao.getNotificationList().blockingFirst()[0]

        // then : updateConfirmedNotification 하면 isNew 값이 false
        assertThat(pushFromDB.articleId, `is`(pushMock.articleId))
        assertThat(pushFromDB.isNew, `is`(false))
    }

    @Test
    fun `getNotificationCount Test`() = runBlocking {
        // 공지가 없으면 0개의 데이터임을 확인
        var notiCountFromDB = pushDao.getNotificationCount(true).blockingFirst()
        assertThat(notiCountFromDB, `is`(0))

        // given
        val pushMock = LocalFixtures.pushEntity()
        pushDao.insertNotification(pushMock)

        // when
        notiCountFromDB = pushDao.getNotificationCount(true).blockingFirst()
        // then : insert 후에 1개의 데이터
        assertThat(notiCountFromDB, `is`(1))

        // when
        pushDao.updateNotificationAsOld(pushMock.articleId, false).blockingSubscribe()
        // then : isNew 를 false 로 업데이트하면 0개의 데이터
        notiCountFromDB = pushDao.getNotificationCount(true).blockingFirst()
        assertThat(notiCountFromDB, `is`(0))
    }

    @Test
    fun `deleteNotification Test`() = runBlocking {
        // 처음엔 0개의 데이터임을 확인
        var notiCountFromDB = pushDao.getNotificationCount(true).blockingFirst()
        assertThat(notiCountFromDB, `is`(0))

        // when
        val pushMock = LocalFixtures.pushEntity()
        pushDao.insertNotification(pushMock)
        // then : insert 후에 1개의 데이터
        notiCountFromDB = pushDao.getNotificationCount(true).blockingFirst()
        assertThat(notiCountFromDB, `is`(1))

        // when
        pushDao.deleteNotification(pushMock.articleId).blockingSubscribe()
        // then : delete 후에 0개의 데이터
        notiCountFromDB = pushDao.getNotificationCount(true).blockingFirst()
        assertThat(notiCountFromDB, `is`(0))
    }
}
