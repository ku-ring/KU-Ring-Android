package com.ku_stacks.ku_ring.academicevent

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ku_stacks.ku_ring.academicevent.mapper.toDomain
import com.ku_stacks.ku_ring.academicevent.mapper.toEntity
import com.ku_stacks.ku_ring.academicevent.repository.AcademicEventRepositoryImpl
import com.ku_stacks.ku_ring.domain.academicevent.repository.AcademicEventRepository
import com.ku_stacks.ku_ring.local.room.AcademicEventDao
import com.ku_stacks.ku_ring.local.room.KuRingDatabase
import com.ku_stacks.ku_ring.remote.academicevent.AcademicEventClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AcademicRepositoryTest {
    private val academicEventClient = Mockito.mock(AcademicEventClient::class.java)
    private lateinit var repository: AcademicEventRepository
    private lateinit var database: KuRingDatabase
    private lateinit var academicEventDao: AcademicEventDao
    private val ioDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(ioDispatcher)
        initDatabase()
        academicEventDao = database.academicEventDao()
        repository =
            AcademicEventRepositoryImpl(academicEventDao, academicEventClient, ioDispatcher)
    }

    private fun initDatabase() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room
            .inMemoryDatabaseBuilder(context, KuRingDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        database.close()
    }

    @Test
    fun `insert and get academic event test`() = runTest {
        //given
        val response = AcademicEventTestUtil.mockAcademicEventListResponse()
        val entities = response.data.map { it.toEntity() }
        academicEventDao.insertAcademicEvents(entities)

        //when
        val result = repository.getAcademicEvents("2025-03-01", "2025-04-30")

        // then
        val expected = entities.first().toDomain()
        with(result.first()) {
            assertEquals(expected.id, id)
            assertEquals(expected.summary, summary)
            assertEquals(expected.category, category)
            assertEquals(expected.startDateTime, startDateTime)
            assertEquals(expected.endDateTime, endDateTime)
        }
    }
}