package com.ku_stacks.ku_ring.remote.library

import com.ku_stacks.ku_ring.remote.library.request.LibrarySeatRequest
import com.ku_stacks.ku_ring.remote.library.response.LibrarySeatResponse
import javax.inject.Inject

class LibraryClient @Inject constructor(private val libraryService: LibraryService) {

    suspend fun fetchRoomSeatStatus(): LibrarySeatResponse = libraryService.fetchLibrarySeatStatus(
        methodCode = LibrarySeatRequest.METHOD_CODE,
        roomTypeId = LibrarySeatRequest.ROOM_TYPE_ID,
        branchGroupId = LibrarySeatRequest.BRANCH_GROUP_ID,
    )
}