package com.ku_stacks.ku_ring.local.dao

import com.ku_stacks.ku_ring.local.LocalDbAbstract
import com.ku_stacks.ku_ring.local.room.AcademicEventDao
import com.ku_stacks.ku_ring.local.test.LocalTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AcademicEventDaoTest : LocalDbAbstract() {
    private lateinit var academicEventDao: AcademicEventDao

    @Before
    fun setup() {
        academicEventDao = db.academicEventDao()
    }

    @Test
    fun `insert and get AcademicEvent test`() = runTest {
        // given
        val entity = LocalTestUtil.fakeAcademicEventEntity()

        // when
        academicEventDao.insertAcademicEvents(listOf(entity))
        val academicEventFromDB = academicEventDao.getAcademicEvents(
            startDate = "2025-03-01",
            endDate = "2025-04-30",
        )

        // then
        assert(academicEventFromDB.isNotEmpty())
        with(academicEventFromDB.first()) {
            assertEquals(2417, id)
            assertEquals("EC66FB8098", eventUid)
            assertEquals("ETC", category)
            assertEquals("2025-03-04T00:00:00", startTime)
            assertEquals("2025-04-03T00:00:00", endTime)
        }
    }
}