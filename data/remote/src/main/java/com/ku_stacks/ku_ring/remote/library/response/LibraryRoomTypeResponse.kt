package com.ku_stacks.ku_ring.remote.library.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryRoomTypeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val roomName: String,
    @SerialName("sortOrder")
    val sortOrder: Int,
)
