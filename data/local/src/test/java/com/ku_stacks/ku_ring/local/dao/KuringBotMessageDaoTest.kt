package com.ku_stacks.ku_ring.local.dao

import com.ku_stacks.ku_ring.local.LocalDbAbstract
import com.ku_stacks.ku_ring.local.room.KuringBotMessageDao
import com.ku_stacks.ku_ring.local.test.LocalTestUtil
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class KuringBotMessageDaoTest : LocalDbAbstract() {
    private lateinit var dao: KuringBotMessageDao

    @Before
    fun setup() {
        dao = db.kuringBotMessageDao()
    }

    @After
    fun tearDown() = runBlocking {
        dao.clear()
    }

    @Test
    fun `insert and get all entities`() = runTest {
        // given + when
        assert(dao.getAllMessages().isEmpty())
        val messages = (0 until 4).map {
            LocalTestUtil.fakeKuringBotMessageEntity(type = 0)
        }
        dao.insertMessages(messages)

        // then
        val allEntities = dao.getAllMessages()
        assertEquals(messages.size, allEntities.size)
    }

    @Test
    fun `insert and get a count of queries by posted time`() = runTest {
        // given
        assert(dao.getAllMessages().isEmpty())
        val postedSecondOffset = 100L
        val messages = (0 until 4).map {
            LocalTestUtil.fakeKuringBotMessageEntity(
                postedEpochSeconds = it + postedSecondOffset,
                type = it % 2,
            )
        }
        dao.insertMessages(messages)

        // when
        val queriesCount = dao.getQueryCount(postedSecondOffset, postedSecondOffset + 3)

        // then
        assertEquals(2, queriesCount)
    }

    @Test
    fun `insert and clear entities`() = runTest {
        // given
        assert(dao.getAllMessages().isEmpty())
        val messages = (0 until 4).map {
            LocalTestUtil.fakeKuringBotMessageEntity(
                postedEpochSeconds = it.toLong(),
                type = 0,
            )
        }
        dao.insertMessages(messages)

        // when
        dao.clear()

        // then
        assert(dao.getAllMessages().isEmpty())
    }
}