package com.ku_stacks.ku_ring.remote

import com.ku_stacks.ku_ring.remote.club.ClubService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ClubServiceTest : ApiAbstract<ClubService>() {

    private lateinit var service: ClubService

    @Before
    fun initService() {
        super.createMockServer()
        service = createService(ClubService::class.java)
    }

    @After
    fun tearDown() {
        super.stopServer()
    }

    @Test
    fun `fetch clubs by division test`() = runTest {
        // given
        enqueueResponse("/ClubListResponse.json")

        // when
        val response = service.getClubs(category = null, division = "central")
        mockWebServer.takeRequest()

        val clubs = response.data ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(3, clubs.clubs.size)

        val first = clubs.clubs[0]
        assertEquals(1, first.id)
        assertEquals("동굴탐사", first.name)
        assertEquals("academic", first.category)
        assertEquals("central", first.division)
        assertNull(first.imageUrl)

        val third = clubs.clubs[2]
        assertEquals(3, third.id)
        assertEquals("우주탐구회", third.name)
        assertNotNull(third.imageUrl)
        assertEquals("2026-02-16T00:00:00", third.recruitStartDate)
    }

    @Test
    fun `fetch club detail test`() = runTest {
        // given
        enqueueResponse("/ClubDetailResponse.json")

        // when
        val response = service.getClubDetail(clubId = 3)
        mockWebServer.takeRequest()

        val club = response.data ?: throw IllegalStateException("Data should not be null")

        // then
        assertEquals(200, response.resultCode)
        assertEquals(3, club.id)
        assertEquals("우주탐구회", club.name)
        assertEquals("academic", club.category)
        assertEquals("central", club.division)
        assertEquals("https://www.instagram.com/konkuk.taurus/", club.instagramUrl)
        assertNull(club.youtubeUrl)
        assertEquals("closed", club.recruitmentStatus)
        assertNotNull(club.descriptionImageUrl)

        val location = club.location ?: throw IllegalStateException("Location should not be null")
        assertEquals("제1학생회관", location.buildingName)
        assertEquals("307호", location.room)
    }
}
