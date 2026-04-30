package com.ku_stacks.ku_ring.remote.library.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryRoomSeatResponse(
    @SerialName("total")
    val total: Int,
    @SerialName("occupied")
    val occupied: Int,
    @SerialName("waiting")
    val waiting: Int,
    @SerialName("available")
    val available: Int,
)
