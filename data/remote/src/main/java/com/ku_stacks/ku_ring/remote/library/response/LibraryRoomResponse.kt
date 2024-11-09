package com.ku_stacks.ku_ring.remote.library.response

import com.google.gson.annotations.SerializedName

data class LibraryRoomResponse(
    val id: Int,
    @SerializedName("name") val roomName: String,
    val roomType: LibraryRoomTypeResponse,
    val awaitable: Boolean,
    val isChargeable: Boolean,
    val branch: LibraryRoomBranchResponse,
    val unableMessage: String?,
    val seats: LibraryRoomSeatResponse
)