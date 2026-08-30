package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.department.DepartmentService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DepartmentServiceTest : ApiAbstract<DepartmentService>() {

    private lateinit var service: DepartmentService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(DepartmentService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch department list test`() = runTest {
        // given
        enqueueResponse("/DepartmentListResponse.json")

        // when
        val response = service.fetchDepartmentList()
        mockWebServer.takeRequest()

        val departments = response.data ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(3, departments.size)

        val first = departments[0]
        assertEquals("computer_science", first.name)
        assertEquals("cse", first.shortName)
        assertEquals("컴퓨터공학부", first.korName)
    }
}
