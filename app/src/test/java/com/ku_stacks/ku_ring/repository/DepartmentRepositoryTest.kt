package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.LocalDbAbstract
import com.ku_stacks.ku_ring.MockUtil
import com.ku_stacks.ku_ring.data.api.DepartmentClient
import com.ku_stacks.ku_ring.data.api.response.DepartmentListResponse
import com.ku_stacks.ku_ring.data.api.response.DepartmentResponse
import com.ku_stacks.ku_ring.data.db.DepartmentDao
import com.ku_stacks.ku_ring.data.mapper.toDepartment
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
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class DepartmentRepositoryTest : LocalDbAbstract() {
    private lateinit var departmentRepository: DepartmentRepository
    private lateinit var departmentDao: DepartmentDao
    private val departmentClient = Mockito.mock(DepartmentClient::class.java)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        departmentDao = db.departmentDao()
        departmentRepository = DepartmentRepositoryImpl(
            departmentDao = departmentDao,
            departmentClient = departmentClient,
            ioDispatcher = testDispatcher,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // integrated test
    @Test
    fun `insert and update departments test`() = runTest {
        // given
        val departments = (1..10).map {
            MockUtil.mockDepartment().copy(
                name = it.toString(),
                shortName = "dep$it",
                koreanName = "학과 $it",
            )
        }
        departmentRepository.insertDepartments(departments)
        assertEquals(departments.size, departmentRepository.getAllDepartments().size)

        // when
        val updatedDepartmentsResponse = departments.map { department ->
            DepartmentResponse(
                name = department.name,
                shortName = department.shortName.repeat(2),
                korName = department.koreanName.repeat(2),
            )
        }
        Mockito.`when`(departmentClient.fetchDepartmentList())
            .thenReturn(DepartmentListResponse(200, "success", updatedDepartmentsResponse))
        departmentRepository.updateDepartmentsFromRemote()

        // then
        val expectedDepartments =
            updatedDepartmentsResponse.map { it.toDepartment() }.sortedBy { it.name }
        val actual = departmentRepository.getAllDepartments().sortedBy { it.name }
        assertEquals(expectedDepartments, actual)
    }
}