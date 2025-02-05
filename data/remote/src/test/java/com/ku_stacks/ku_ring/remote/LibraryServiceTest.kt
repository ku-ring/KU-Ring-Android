package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.library.LibraryService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LibraryServiceTest : ApiAbstract<LibraryService>() {

    private lateinit var service: LibraryService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(LibraryService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch Library Seat Status From Network Test`() = runTest {
        // given
        enqueueResponse("/LibrarySeatsResponse.json")

        // when
        val response = service.fetchLibrarySeatStatus(
            methodCode = "PC",
            roomTypeId = 4,
            branchGroupId = 1,
        )
        mockWebServer.takeRequest()

        // then
        assertEquals(true, response.success)
        assertEquals("조회되었습니다.", response.message)
        assertEquals(7, response.data.resultCount)
        assertEquals("제 1열람실 (A구역)", response.data.libraryRooms[0].roomName)
        assertEquals(true, response.data.libraryRooms[0].isChargeable)
    }


}