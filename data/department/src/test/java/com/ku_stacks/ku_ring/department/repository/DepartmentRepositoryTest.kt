package com.ku_stacks.ku_ring.department.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ku_stacks.ku_ring.department.UnsupportedDepartmentUtil
import com.ku_stacks.ku_ring.department.mapper.toDepartment
import com.ku_stacks.ku_ring.department.test.DepartmentTestUtil
import com.ku_stacks.ku_ring.local.room.DepartmentDao
import com.ku_stacks.ku_ring.local.room.KuRingDatabase
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.department.DepartmentClient
import com.ku_stacks.ku_ring.remote.department.response.DepartmentListResponse
import com.ku_stacks.ku_ring.remote.department.response.DepartmentResponse
import junit.framework.Assert
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
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
@Config(sdk = [31])
class DepartmentRepositoryTest {
    private lateinit var departmentRepository: DepartmentRepository
    private lateinit var departmentDao: DepartmentDao
    private val departmentClient = Mockito.mock(DepartmentClient::class.java)
    private lateinit var pref: PreferenceUtil
    private lateinit var db: KuRingDatabase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        pref = PreferenceUtil(ApplicationProvider.getApplicationContext())
        initDB()
        departmentDao = db.departmentDao()
        departmentRepository = DepartmentRepositoryImpl(
            departmentDao = departmentDao,
            departmentClient = departmentClient,
            pref = pref,
            ioDispatcher = testDispatcher,
        )
    }

    private fun initDB() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            KuRingDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        closeDB()
    }

    private fun closeDB() {
        db.close()
    }

    // integrated test
    @Test
    fun `insert and update departments test`() = runTest {
        // given
        val departments = (1..10).map {
            DepartmentTestUtil.fakeDepartment(
                name = it.toString(),
                shortName = "dep$it",
                koreanName = "학과 $it",
            )
        }
        departmentRepository.insertDepartments(departments)
        assertEquals(departments.size, departmentRepository.getAllDepartments().size)

        // when
        val updatedDepartmentsResponse =
            departments.mapIndexed { index, (name, shortName, koreanName, _) ->
                DepartmentResponse(
                    name = name,
                    shortName = if (index % 2 == 0) shortName else shortName.repeat(2),
                    korName = if (index % 2 == 0) koreanName else koreanName.repeat(2),
                )
            }
        Mockito.`when`(departmentClient.fetchDepartmentList())
            .thenReturn(DepartmentListResponse(200, "success", updatedDepartmentsResponse))
        departmentRepository.updateDepartmentsFromRemote()

        // then
        val expectedDepartments =
            updatedDepartmentsResponse.map { it.toDepartment() }.sortedBy { it.name }
        val actual = departmentRepository.getAllDepartments().sortedBy { it.name }
            .filterNot { UnsupportedDepartmentUtil.isUnsupportedDepartment(it.name) }
        assertEquals(expectedDepartments, actual)
    }

    @Test
    fun `save Subscription To Local Test`() {
        // given
        val mockData = arrayListOf("학사", "취창업")

        // when
        pref.saveSubscriptionFromKorean(mockData)

        // then
        val expected = arrayListOf("bch", "emp").toSet()
        Assert.assertEquals(expected, pref.subscription)
    }
}