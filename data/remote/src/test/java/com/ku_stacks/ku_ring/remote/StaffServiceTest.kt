package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.staff.StaffService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StaffServiceTest : ApiAbstract<StaffService>() {

    private lateinit var service: StaffService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(StaffService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch staffs by name test`() = runTest {
        // given
        enqueueResponse("/StaffSearchResponse.json")

        // when
        val response = service.fetchStaffs(content = "김은이")
        mockWebServer.takeRequest()

        val staffData = response.data ?: throw IllegalStateException("Data should not be null")
        val staffList =
            staffData.staffList ?: throw IllegalStateException("StaffList should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(1, staffList.size)

        val staff = staffList[0]
        assertEquals("김은이 (Eun Yi KIm)", staff.name)
        assertEquals("eykim@konkuk.ac.kr", staff.email)
        assertEquals("컴퓨터공학부", staff.deptName)
        assertEquals("공과대학", staff.collegeName)
    }
}
