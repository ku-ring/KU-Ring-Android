package com.ku_stacks.ku_ring.remote.library.response

import com.google.gson.annotations.SerializedName

data class LibraryRoomListResponse(
    @SerializedName(value = "totalCount")
    val resultCount: Int,
    @SerializedName(value = "list")
    val libraryRooms: List<LibraryRoomResponse>
)