package com.ku_stacks.ku_ring.remote.library.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryRoomListResponse(
    @SerialName(value = "totalCount")
    val resultCount: Int,
    @SerialName(value = "list")
    val libraryRooms: List<LibraryRoomResponse>,
)