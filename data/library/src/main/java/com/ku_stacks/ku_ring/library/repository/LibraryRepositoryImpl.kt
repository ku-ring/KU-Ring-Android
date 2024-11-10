package com.ku_stacks.ku_ring.library.repository

import com.ku_stacks.ku_ring.domain.LibraryRoom
import com.ku_stacks.ku_ring.library.mapper.toLibraryAreaList
import com.ku_stacks.ku_ring.remote.library.LibraryClient
import retrofit2.HttpException
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val libraryClient: LibraryClient
): LibraryRepository {
    override suspend fun getRemainingSeats(): List<LibraryRoom> {
        return try {
            libraryClient.fetchRoomSeatStatus().toLibraryAreaList()
        } catch (e: HttpException) {
            emptyList()
        }
    }
}