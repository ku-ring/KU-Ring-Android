package com.ku_stacks.ku_ring.network

import com.ku_stacks.ku_ring.data.api.SendbirdService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SendbirdServiceTest : ApiAbstract<SendbirdService>() {

    private lateinit var service: SendbirdService

    @Before
    fun initService() {
        service = createService(SendbirdService::class.java)
    }

    @Test
    fun `fetch Nickname List Test`() {
        // given
        val mockNickname = "건국오리들입니다"
        val mockId = "jj7g68k6pf@privaterelay.appleid.com"
        enqueueResponse("/UserListResponse.json")

        // when
        val response = service.fetchNicknameList(mockNickname).blockingGet()
        mockWebServer.takeRequest()

        // then
        assertEquals(1, response.users.size)
        assertEquals(mockNickname, response.users[0].nickname)
        assertEquals(mockId, response.users[0].userId)
    }
}