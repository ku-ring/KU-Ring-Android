package com.ku_stacks.ku_ring.remote.library.response

data class LibraryRoomSeatResponse(
    val total: Int,
    val occupied: Int,
    val waiting: Int,
    val available: Int
)
