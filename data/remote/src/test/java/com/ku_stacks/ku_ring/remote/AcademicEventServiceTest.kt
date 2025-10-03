package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.academicevent.AcademicEventService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AcademicEventServiceTest : ApiAbstract<AcademicEventService>() {

    private lateinit var service: AcademicEventService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(AcademicEventService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch Academic Events From Network Test`() = runTest {
        // given
        enqueueResponse("/AcademicEventResponse.json")

        // when
        val response = service.fetchAcademicEvents()
        mockWebServer.takeRequest()

        // then
        with(response.data.first()) {
            assertEquals(2417, id)
            assertEquals("EC66FB8098", eventUid)
            assertEquals("ETC", category)
            assertEquals("2025-03-04T00:00:00", startTime)
            assertEquals("2025-04-03T00:00:00", endTime)
        }
    }
}
