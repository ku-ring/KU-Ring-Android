package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.space.KuringSpaceService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KuringSpaceServiceTest : ApiAbstract<KuringSpaceService>() {

    private lateinit var service: KuringSpaceService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(KuringSpaceService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch minimum app version test`() = runTest {
        // given
        enqueueResponse("/AppVersionResponse.json")

        // when
        val response = service.getMinimumAppVersion()
        mockWebServer.takeRequest()

        // then
        assertEquals("2.0.0", response.minimumAppVersion)
    }
}
