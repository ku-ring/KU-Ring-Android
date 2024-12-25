package com.ku_stacks.ku_ring.library.repository

import com.ku_stacks.ku_ring.domain.LibraryRoom
import com.ku_stacks.ku_ring.library.mapper.toLibraryAreaList
import com.ku_stacks.ku_ring.remote.library.LibraryClient
import com.ku_stacks.ku_ring.util.suspendRunCatching
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val libraryClient: LibraryClient
) : LibraryRepository {
    override suspend fun getRemainingSeats(): Result<List<LibraryRoom>> = suspendRunCatching {
        libraryClient.fetchRoomSeatStatus().toLibraryAreaList()
    }
}