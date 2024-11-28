package com.ku_stacks.ku_ring.remote.library.response

import com.google.gson.annotations.SerializedName

data class LibraryRoomResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val roomName: String,
    @SerializedName("roomType")
    val roomType: LibraryRoomTypeResponse,
    @SerializedName("awaitable")
    val awaitable: Boolean,
    @SerializedName("isChargeable")
    val isChargeable: Boolean,
    @SerializedName("branch")
    val branch: LibraryRoomBranchResponse,
    @SerializedName("unableMessage")
    val unableMessage: String?,
    @SerializedName("seats")
    val seats: LibraryRoomSeatResponse,
)