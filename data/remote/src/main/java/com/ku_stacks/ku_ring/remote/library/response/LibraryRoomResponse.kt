package com.ku_stacks.ku_ring.remote.library.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryRoomResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val roomName: String,
    @SerialName("roomType")
    val roomType: LibraryRoomTypeResponse,
    @SerialName("isChargeable")
    val isChargeable: Boolean,
    @SerialName("branch")
    val branch: LibraryRoomBranchResponse,
    @SerialName("unableMessage")
    val unableMessage: String?,
    @SerialName("seats")
    val seats: LibraryRoomSeatResponse,
)