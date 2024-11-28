package com.ku_stacks.ku_ring.library

import com.ku_stacks.ku_ring.library.repository.LibraryRepository
import com.ku_stacks.ku_ring.library.repository.LibraryRepositoryImpl
import com.ku_stacks.ku_ring.remote.library.LibraryClient
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.times

class LibraryRepositoryTest {
    private lateinit var libraryRepository: LibraryRepository
    private val client: LibraryClient = Mockito.mock(LibraryClient::class.java)

    @Before
    fun setup() {
        libraryRepository = LibraryRepositoryImpl(client)
    }

    @Test
    fun `get Library Seat Status From Remote Test`() = runTest {
        val mockLibraryStatus = LibraryTestUtil.mockLibrarySeatResponse()

        Mockito.`when`(client.fetchRoomSeatStatus()).thenReturn(mockLibraryStatus)

       libraryRepository.getRemainingSeats().onSuccess { mockResult ->
            val expectedResult = LibraryTestUtil.mockLibraryRoomList()

            Mockito.verify(
                client,
                times(1)
            ).fetchRoomSeatStatus()

            assertEquals(mockResult, expectedResult)
       }
    }
}