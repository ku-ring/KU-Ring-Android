package com.ku_stacks.ku_ring.remote.library.response

data class LibrarySeatResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: LibraryRoomListResponse
)