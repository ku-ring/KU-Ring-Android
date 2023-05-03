package com.ku_stacks.ku_ring.persistence

import com.ku_stacks.ku_ring.data.db.PageKeyDao
import com.ku_stacks.ku_ring.data.db.PageKeyEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
@Config(sdk = [24])
class PageKeyDaoTest : LocalDbAbstract() {

    private lateinit var pageKeyDao: PageKeyDao

    @Before
    fun init() {
        pageKeyDao = db.pageKeyDao()
    }

    @Test
    fun `insert and get PageKey Test`() = runTest {
        // given
        val pageKeyEntity = PageKeyEntity(articleId = "123456", page = 1)
        pageKeyDao.insertPageKeys(listOf(pageKeyEntity))

        // when
        val storedEntity = pageKeyDao.getPageKey(pageKeyEntity.articleId)

        // then
        assertThat(pageKeyEntity, `is`(storedEntity))
    }

}